package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.entity.*;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.VentaMapper;
import com.chancla.chancla_lite_auth.repository.*;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import com.chancla.chancla_lite_auth.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final MetodoPagoRepository metodoPagoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;
    private final VentaMapper ventaMapper;
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    public VentaServiceImpl(VentaRepository ventaRepository,
                            DetalleVentaRepository detalleVentaRepository,
                            ProductoRepository productoRepository,
                            SesionTrabajoRepository sesionTrabajoRepository,
                            MetodoPagoRepository metodoPagoRepository,
                            UsuarioRepository usuarioRepository,
                            AuditoriaService auditoriaService,
                            VentaMapper ventaMapper,
                            MovimientoInventarioRepository movimientoInventarioRepository) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoRepository = productoRepository;
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.metodoPagoRepository = metodoPagoRepository;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        this.ventaMapper = ventaMapper;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }
    @Override
    public VentaResponse procesarVenta(VentaRequest request) {
        // 1. Validar Sesión y Permisos
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        SesionTrabajoEntity sesion = null;

        // Lógica de atribución de usuario para Administradores
        if (isAdmin && request.getUsuarioId() != null) {
            // Intentar encontrar una sesión abierta para el usuario seleccionado
            sesion = sesionTrabajoRepository.findByUsuarioIdAndEstado(request.getUsuarioId(), EstadoSesion.ABIERTA)
                    .orElseGet(() -> {
                        // Si no hay sesión abierta, buscar la última sesión de ese usuario
                        List<SesionTrabajoEntity> sesiones = sesionTrabajoRepository.findByUsuarioId(request.getUsuarioId());
                        if (!sesiones.isEmpty()) {
                            return sesiones.get(sesiones.size() - 1);
                        }
                        
                        // Si el usuario no tiene ninguna sesión, crear una automática
                        UsuarioEntity usuario = usuarioRepository.findById(request.getUsuarioId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado ID: " + request.getUsuarioId()));
                        
                        SesionTrabajoEntity nuevaSesion = new SesionTrabajoEntity();
                        nuevaSesion.setUsuario(usuario);
                        nuevaSesion.setEstado(EstadoSesion.CERRADA); // Se crea cerrada por ser histórica/automática
                        nuevaSesion.setRolUsuario(usuario.getRol().getNombre());
                        
                        // Establecer fecha/hora basada en la fecha histórica o actual
                        java.time.LocalDateTime fechaRef = (request.getFechaHistorica() != null) 
                            ? request.getFechaHistorica().atTime(9, 0) 
                            : java.time.LocalDateTime.now();
                            
                        nuevaSesion.setHoraInicio(fechaRef);
                        nuevaSesion.setHoraFin(fechaRef.plusHours(8));
                        
                        return sesionTrabajoRepository.save(nuevaSesion);
                    });
        } else {
            // Flujo normal por sesionId
            sesion = sesionTrabajoRepository.findById(request.getSesionId())
                    .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));
        }

        // Regla: Bloquea si la sesión no está abierta (excepto para administradores)
        if (!isAdmin && (sesion == null || sesion.getEstado() != EstadoSesion.ABIERTA)) {
            throw new RuntimeException("La sesión no está abierta. No se puede procesar la venta.");
        }

        if (sesion == null) {
             throw new RuntimeException("No se pudo determinar una sesión para procesar la venta. Los administradores deben seleccionar un vendedor.");
        }

        // 2. Validar Método de Pago
        MetodoPagoEntity metodoPago = metodoPagoRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado."));

        // 3. Crear cabecera de Venta
        VentaEntity venta = new VentaEntity();
        venta.setSesion(sesion);
        venta.setMetodoPago(metodoPago);
        venta.setNumeroFactura(generarNumeroFactura());
        venta.setDescuento(request.getDescuento());
        
        if (request.getFechaHistorica() != null) {
            // Validar que el usuario sea ADMIN para usar fecha histórica
            if (!isAdmin) {
                throw new RuntimeException("No tienes permisos para registrar ventas con fecha histórica. Solo administradores pueden realizar esta acción.");
            }
            venta.setFechaRegistroManual(request.getFechaHistorica().atTime(12, 0));
        } else {
            venta.setFechaRegistroManual(LocalDateTime.now());
        }
        
        Double subtotal = 0.0;
        List<DetalleVentaEntity> detalles = new ArrayList<>();

        // 4. Procesar Detalles y Stock (con bloqueo pesimista para evitar sobreventa)
        for (VentaRequest.DetalleVentaRequest detReq : request.getDetalles()) {
            ProductoEntity producto = productoRepository.findByIdForUpdate(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detReq.getProductoId()));

            if (producto.getStockActual() < detReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + 
                        " (Pedido: " + detReq.getCantidad() + ", Disponible: " + producto.getStockActual() + ")");
            }

            // Reemplazo del Trigger: Descontar stock e insertar movimiento de inventario
            producto.setStockActual(producto.getStockActual() - detReq.getCantidad());
            productoRepository.save(producto);
            
            // Crear Detalle
            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detReq.getCantidad());
            
            if (detReq.getPrecioUnitario() != null) {
                detalle.setPrecioUnitario(detReq.getPrecioUnitario());
            } else {
                detalle.setPrecioUnitario(producto.getPrecioVenta());
            }
            
            detalle.setCostoUnitario(producto.getPrecioCompra());
            
            detalles.add(detalle);
            subtotal += (detalle.getPrecioUnitario() * detalle.getCantidad());
        }

        venta.setSubtotal(subtotal);
        venta.setTotal(subtotal - request.getDescuento());

        VentaEntity ventaGuardada = ventaRepository.save(venta);
        
        // Registro de Auditoría
        String usuarioActual = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        String detallesLog = String.format("Venta %s creada por un monto de %f. Vendedor asignado: %s", 
            ventaGuardada.getNumeroFactura(), ventaGuardada.getTotal(), sesion.getUsuario().getNombre());
        auditoriaService.registrar(usuarioActual, "CREAR", "VENTA", ventaGuardada.getId(), detallesLog);
        
        // Asignar referenciaId a los detalles y crear movimientos de inventario
        for (DetalleVentaEntity d : detalles) {
            d.setVenta(ventaGuardada);
            detalleVentaRepository.save(d);
            
            // Registrar Movimiento de Inventario
            MovimientoInventarioEntity mov = new MovimientoInventarioEntity();
            mov.setProducto(d.getProducto());
            mov.setTipo(TipoMovimiento.SALIDA);
            mov.setCantidad(d.getCantidad());
            mov.setMotivo("Venta");
            mov.setReferenciaId(ventaGuardada.getId());
            movimientoInventarioRepository.save(mov);
        }

        return ventaMapper.toResponse(ventaGuardada, ventaMapper.toDetalleResponseList(detalles));
    }

    private String generarNumeroFactura() {
        LocalDateTime now = LocalDateTime.now();
        String prefix = "VEN-";
        String datePart = now.format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefix + datePart + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorId(Long id) {
        VentaEntity venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));
        List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(id);
        return ventaMapper.toResponse(venta, ventaMapper.toDetalleResponseList(detalles));
    }

    @Override
    @Transactional(readOnly = true)
    public VentaResponse obtenerPorNumeroFactura(String numeroFactura) {
        VentaEntity venta = ventaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));
        List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(venta.getId());
        return ventaMapper.toResponse(venta, ventaMapper.toDetalleResponseList(detalles));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerTodas() {
        return ventaRepository.findAll().stream()
                .map(v -> ventaMapper.toResponse(v, ventaMapper.toDetalleResponseList(v.getDetalles())))
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorSesion(Long sesionId) {
        return ventaRepository.findBySesionId(sesionId).stream()
                .map(v -> ventaMapper.toResponse(v, ventaMapper.toDetalleResponseList(v.getDetalles())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorUsuario(Long usuarioId) {
        return ventaRepository.findBySesionUsuarioId(usuarioId).stream()
                .map(v -> ventaMapper.toResponse(v, ventaMapper.toDetalleResponseList(v.getDetalles())))
                .toList();
    }

    @Override
    public VentaResponse actualizarVenta(Long id, VentaRequest request) {
        // 1. Validar Permisos (Solo Admin)
        boolean isAdmin = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));
        
        if (!isAdmin) {
            throw new RuntimeException("No tienes permisos para editar ventas.");
        }

        VentaEntity venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada."));

        // 2. Validar Método de Pago
        MetodoPagoEntity metodoPago = metodoPagoRepository.findById(request.getMetodoPagoId())
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado."));
        
        venta.setMetodoPago(metodoPago);
        venta.setDescuento(request.getDescuento());

        // 3. Gestionar Detalles
        // Reversar detalles antiguos: recuperar stock y registrar movimiento de entrada (con bloqueo pesimista)
        List<DetalleVentaEntity> detallesAntiguos = detalleVentaRepository.findByVentaId(id);
        for (DetalleVentaEntity detAntiguo : detallesAntiguos) {
            ProductoEntity prodAntiguo = productoRepository.findByIdForUpdate(detAntiguo.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detAntiguo.getProducto().getId()));
            prodAntiguo.setStockActual(prodAntiguo.getStockActual() + detAntiguo.getCantidad());
            productoRepository.save(prodAntiguo);
            
            MovimientoInventarioEntity movReverso = new MovimientoInventarioEntity();
            movReverso.setProducto(prodAntiguo);
            movReverso.setTipo(TipoMovimiento.ENTRADA);
            movReverso.setCantidad(detAntiguo.getCantidad());
            movReverso.setMotivo("Reverso edición venta");
            movReverso.setReferenciaId(venta.getId());
            movimientoInventarioRepository.save(movReverso);
        }
        detalleVentaRepository.deleteByVentaId(id);
        
        Double subtotal = 0.0;
        List<DetalleVentaEntity> nuevosDetalles = new ArrayList<>();

        for (VentaRequest.DetalleVentaRequest detReq : request.getDetalles()) {
            ProductoEntity producto = productoRepository.findByIdForUpdate(detReq.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + detReq.getProductoId()));

            if (producto.getStockActual() < detReq.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre() + 
                        " (Pedido: " + detReq.getCantidad() + ", Disponible: " + producto.getStockActual() + ")");
            }

            // Descontar nuevo stock
            producto.setStockActual(producto.getStockActual() - detReq.getCantidad());
            productoRepository.save(producto);

            DetalleVentaEntity detalle = new DetalleVentaEntity();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(detReq.getCantidad());
            
            if (detReq.getPrecioUnitario() != null) {
                detalle.setPrecioUnitario(detReq.getPrecioUnitario());
            } else {
                detalle.setPrecioUnitario(producto.getPrecioVenta());
            }
            
            detalle.setCostoUnitario(producto.getPrecioCompra());
            
            nuevosDetalles.add(detalle);
            subtotal += (detalle.getPrecioUnitario() * detalle.getCantidad());
        }

        venta.setSubtotal(subtotal);
        venta.setTotal(subtotal - request.getDescuento());

        VentaEntity ventaGuardada = ventaRepository.save(venta);
        
        // Guardar detalles y movimientos de inventario nuevos
        for (DetalleVentaEntity d : nuevosDetalles) {
            d.setVenta(ventaGuardada);
            detalleVentaRepository.save(d);
            
            MovimientoInventarioEntity mov = new MovimientoInventarioEntity();
            mov.setProducto(d.getProducto());
            mov.setTipo(TipoMovimiento.SALIDA);
            mov.setCantidad(d.getCantidad());
            mov.setMotivo("Cambio de producto en venta");
            mov.setReferenciaId(ventaGuardada.getId());
            movimientoInventarioRepository.save(mov);
        }

        // Auditoría
        String usuarioActual = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaService.registrar(usuarioActual, "EDITAR", "VENTA", ventaGuardada.getId(), 
            "Venta " + ventaGuardada.getNumeroFactura() + " editada por administrador.");

        return ventaMapper.toResponse(ventaGuardada, ventaMapper.toDetalleResponseList(nuevosDetalles));
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaResponse> obtenerPorRango(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        return ventaRepository.findByRangoFechas(inicio, fin).stream()
                .map(v -> ventaMapper.toResponse(v, ventaMapper.toDetalleResponseList(v.getDetalles())))
                .toList();
    }
}
