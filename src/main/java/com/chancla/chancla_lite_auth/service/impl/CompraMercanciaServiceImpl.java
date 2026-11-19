package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.CompraMercanciaRequest;
import com.chancla.chancla_lite_auth.dto.request.DetalleCompraRequest;
import com.chancla.chancla_lite_auth.dto.response.CompraMercanciaResponse;
import com.chancla.chancla_lite_auth.entity.CompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.DetalleCompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.CompraMercanciaMapper;
import com.chancla.chancla_lite_auth.repository.CompraMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.DetalleCompraMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.CompraMercanciaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CompraMercanciaServiceImpl implements CompraMercanciaService {

    private final CompraMercanciaRepository compraMercanciaRepository;
    private final DetalleCompraMercanciaRepository detalleCompraMercanciaRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final CompraMercanciaMapper compraMercanciaMapper;

    public CompraMercanciaServiceImpl(CompraMercanciaRepository compraMercanciaRepository,
                                      DetalleCompraMercanciaRepository detalleCompraMercanciaRepository,
                                      ProductoRepository productoRepository,
                                      MovimientoInventarioRepository movimientoInventarioRepository,
                                      UsuarioRepository usuarioRepository,
                                      CompraMercanciaMapper compraMercanciaMapper) {
        this.compraMercanciaRepository = compraMercanciaRepository;
        this.detalleCompraMercanciaRepository = detalleCompraMercanciaRepository;
        this.productoRepository = productoRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.usuarioRepository = usuarioRepository;
        this.compraMercanciaMapper = compraMercanciaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraMercanciaResponse> listar() {
        List<CompraMercanciaResponse> response = new ArrayList<>();
        for (CompraMercanciaEntity compra : compraMercanciaRepository.findAllByOrderByFechaDescCreatedAtDesc()) {
            response.add(compraMercanciaMapper.toResponse(compra,
                    detalleCompraMercanciaRepository.findByCompraId(compra.getId())));
        }
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public CompraMercanciaResponse obtenerPorId(Long id) {
        CompraMercanciaEntity compra = compraMercanciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compra de mercancía no encontrada."));
        return compraMercanciaMapper.toResponse(compra,
                detalleCompraMercanciaRepository.findByCompraId(compra.getId()));
    }

    @Override
    public CompraMercanciaResponse crear(CompraMercanciaRequest request) {
        List<DetalleCompraRequest> lineas = new ArrayList<>(request.getLineas());
        lineas.sort(Comparator.comparing(DetalleCompraRequest::getProductoId));

        Set<Long> productosVistos = new HashSet<>();
        double total = 0.0;
        for (DetalleCompraRequest linea : lineas) {
            if (!productosVistos.add(linea.getProductoId())) {
                throw new RuntimeException("El producto está repetido en la compra. Unifícalo en una sola línea.");
            }
            total += linea.getCostoUnitario() * linea.getCantidad();
        }

        CompraMercanciaEntity compra = new CompraMercanciaEntity();
        compra.setFecha(request.getFecha());
        compra.setProveedor(limpiar(request.getProveedor(), 150));
        compra.setDescripcion(limpiar(request.getDescripcion(), 200));
        compra.setMetodoPago(request.getMetodoPago());
        compra.setMontoTotal(total);
        compra.setDetallada(true);
        compra.setUsuario(usuarioActual());
        compra = compraMercanciaRepository.save(compra);

        List<DetalleCompraMercanciaEntity> detalles = new ArrayList<>();
        for (DetalleCompraRequest linea : lineas) {
            ProductoEntity producto = productoRepository.findByIdForUpdate(linea.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + linea.getProductoId()));

            producto.setStockActual(producto.getStockActual() + linea.getCantidad());
            producto.setPrecioCompra(linea.getCostoUnitario());
            if (linea.getPrecioVenta() != null) {
                producto.setPrecioVenta(linea.getPrecioVenta());
            }
            productoRepository.save(producto);

            DetalleCompraMercanciaEntity detalle = new DetalleCompraMercanciaEntity();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(linea.getCantidad());
            detalle.setCostoUnitario(linea.getCostoUnitario());
            detalle.setSubtotal(linea.getCostoUnitario() * linea.getCantidad());
            detalle = detalleCompraMercanciaRepository.save(detalle);
            detalles.add(detalle);

            MovimientoInventarioEntity movimiento = new MovimientoInventarioEntity();
            movimiento.setProducto(producto);
            movimiento.setTipo(TipoMovimiento.ENTRADA);
            movimiento.setCantidad(linea.getCantidad());
            movimiento.setCostoUnitario(linea.getCostoUnitario());
            movimiento.setEsCompra(true);
            movimiento.setMotivo(motivoCompra(compra));
            movimiento.setReferenciaId(compra.getId());
            movimientoInventarioRepository.save(movimiento);
        }

        return compraMercanciaMapper.toResponse(compra, detalles);
    }

    private String motivoCompra(CompraMercanciaEntity compra) {
        String motivo = "Compra de mercancia #" + compra.getId();
        String proveedor = compra.getProveedor();
        if (proveedor != null && !proveedor.isBlank()) {
            int disponible = 100 - motivo.length() - 3;
            String recortado = proveedor.length() > disponible
                    ? proveedor.substring(0, Math.max(0, disponible))
                    : proveedor;
            motivo += " - " + recortado;
        }
        return motivo;
    }

    private String limpiar(String valor, int max) {
        if (valor == null) {
            return null;
        }
        String limpio = valor.trim();
        if (limpio.isEmpty()) {
            return null;
        }
        return limpio.length() > max ? limpio.substring(0, max) : limpio;
    }

    private UsuarioEntity usuarioActual() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            return usuarioRepository.findByEmail(email).orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}
