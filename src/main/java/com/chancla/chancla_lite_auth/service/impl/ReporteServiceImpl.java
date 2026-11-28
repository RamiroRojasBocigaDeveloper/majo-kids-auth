package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.entity.CompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import com.chancla.chancla_lite_auth.enums.MetodoPagoCompra;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.CompraMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.DetalleVentaRepository;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.repository.SaldoInicialMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.SueldoPagadoRepository;
import com.chancla.chancla_lite_auth.repository.VentaRepository;
import com.chancla.chancla_lite_auth.mapper.GastoMapper;
import com.chancla.chancla_lite_auth.mapper.VentaMapper;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private static final String CATEGORIA_GASTOS_OPERATIVOS = "Logistica para surtir";

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final SueldoPagadoRepository sueldoPagadoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final SaldoInicialMercanciaRepository saldoInicialMercanciaRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final CompraMercanciaRepository compraMercanciaRepository;
    private final ProductoRepository productoRepository;
    private final GastoMapper gastoMapper;
    private final VentaMapper ventaMapper;

    @Autowired
    public ReporteServiceImpl(VentaRepository ventaRepository,
                              GastoRepository gastoRepository,
                              SueldoPagadoRepository sueldoPagadoRepository,
                              DetalleVentaRepository detalleVentaRepository,
                              SaldoInicialMercanciaRepository saldoInicialMercanciaRepository,
                              MovimientoInventarioRepository movimientoInventarioRepository,
                              CompraMercanciaRepository compraMercanciaRepository,
                              ProductoRepository productoRepository,
                              GastoMapper gastoMapper,
                              VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.gastoRepository = gastoRepository;
        this.sueldoPagadoRepository = sueldoPagadoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.saldoInicialMercanciaRepository = saldoInicialMercanciaRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.compraMercanciaRepository = compraMercanciaRepository;
        this.productoRepository = productoRepository;
        this.gastoMapper = gastoMapper;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public DashboardResponse obtenerResumenPorSesion(Long sesionId) {
        List<VentaEntity> ventas = ventaRepository.findBySesionId(sesionId);
        List<GastoEntity> gastos = gastoRepository.findBySesionId(sesionId);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findBySesionId(sesionId);

        return construirDashboard(ventas, gastos, sueldos, List.of());
    }

    @Override
    public DashboardResponse obtenerResumenPorRangoFechas(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<VentaEntity> ventas = ventaRepository.findByRangoFechas(inicioDateTime, finDateTime);
        List<GastoEntity> gastos = gastoRepository.findByRangoFechas(inicioDateTime, finDateTime);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findByFechaPagoBetween(inicio, fin);
        List<CompraMercanciaEntity> compras = compraMercanciaRepository
                .findByFechaBetweenOrderByFechaDescCreatedAtDesc(inicio, fin);

        return construirDashboard(ventas, gastos, sueldos, compras);
    }

    @Override
    public DashboardResponse obtenerResumenGlobal() {
        List<CompraMercanciaEntity> compras = compraMercanciaRepository.findAllByOrderByFechaDescCreatedAtDesc();
        return construirDashboard(ventaRepository.findAll(), gastoRepository.findAll(), sueldoPagadoRepository.findAll(), compras);
    }

    private DashboardResponse construirDashboard(List<VentaEntity> ventas, List<GastoEntity> gastos, List<SueldoPagadoEntity> sueldos, List<CompraMercanciaEntity> compras) {
        DashboardResponse dashboard = new DashboardResponse();
        
        double totalVentas = ventas.stream().mapToDouble(VentaEntity::getTotal).sum();
        double totalGastos = gastos.stream().mapToDouble(GastoEntity::getMonto).sum();
        double totalSueldos = sueldos.stream().mapToDouble(SueldoPagadoEntity::getMonto).sum();
        double comprasMercancia = compras.stream()
                .mapToDouble(c -> c.getMontoTotal() != null ? c.getMontoTotal() : 0.0)
                .sum();

        // Cálculo de Ganancia Real basada en el Margen de los productos (PrecioVenta - PrecioCosto)
        double gananciaProductos = 0.0;
        double costoMercancia = 0.0;

        if (!ventas.isEmpty()) {
            gananciaProductos = detalleVentaRepository.sumMargenByVentas(ventas);
            costoMercancia = detalleVentaRepository.sumCostoByVentas(ventas);
        }

        dashboard.setTotalVentas(totalVentas);
        dashboard.setTotalGastos(totalGastos);
        dashboard.setTotalSueldos(totalSueldos);
        dashboard.setGananciaProductos(gananciaProductos);
        dashboard.setCostoMercancia(costoMercancia);
        dashboard.setComprasMercancia(comprasMercancia);

        // Utilidad neta = margen de productos - gastos operativos - nomina.
        // Las compras de mercancia NO restan utilidad: se convierten en costo cuando se venden.
        dashboard.setUtilidadNeta(gananciaProductos - (totalGastos + totalSueldos));

        // Capital disponible = dinero producido por el almacen menos gastos, nomina y compras de surtido.
        // La inversion inicial (saldo de apertura) NO entra aqui.
        double capitalDisponible = totalVentas - (totalGastos + totalSueldos) - comprasMercancia;
        dashboard.setCapitalDisponible(capitalDisponible);
        dashboard.setGananciaNeta(capitalDisponible);

        double ventasEfectivo = ventas.stream()
                .filter(v -> v.getMetodoPago() != null && esEfectivo(v.getMetodoPago().getNombre()))
                .mapToDouble(VentaEntity::getTotal)
                .sum();
        double ventasNoEfectivo = totalVentas - ventasEfectivo;
        double gastosEfectivo = gastos.stream()
                .filter(GastoEntity::isEsDeCaja)
                .mapToDouble(GastoEntity::getMonto)
                .sum();
        double gastosNoEfectivo = totalGastos - gastosEfectivo;
        double comprasEfectivo = compras.stream()
                .filter(c -> MetodoPagoCompra.EFECTIVO.equals(c.getMetodoPago()))
                .mapToDouble(c -> c.getMontoTotal() != null ? c.getMontoTotal() : 0.0)
                .sum();
        double comprasNoEfectivo = comprasMercancia - comprasEfectivo;

        dashboard.setVentasEfectivo(ventasEfectivo);
        dashboard.setVentasNoEfectivo(ventasNoEfectivo);
        dashboard.setGastosEfectivo(gastosEfectivo);
        dashboard.setGastosNoEfectivo(gastosNoEfectivo);
        dashboard.setComprasEfectivo(comprasEfectivo);
        dashboard.setComprasNoEfectivo(comprasNoEfectivo);
        dashboard.setCajaNetaEfectivo(ventasEfectivo - gastosEfectivo - totalSueldos - comprasEfectivo);
        dashboard.setCajaNetaNoEfectivo(ventasNoEfectivo - gastosNoEfectivo - comprasNoEfectivo);
        dashboard.setCantidadVentas((long) ventas.size());

        // Agrupación por método de pago
        Map<String, Double> ventasPorMetodo = ventas.stream()
                .collect(Collectors.groupingBy(
                        v -> v.getMetodoPago().getNombre(),
                        Collectors.summingDouble(VentaEntity::getTotal)
                ));
        dashboard.setVentasPorMetodoPago(ventasPorMetodo);

        // Agrupación por Categoría y Productos más vendidos
        Map<String, Long> productosFavoritos = new HashMap<>();
        Map<String, Double> ventasPorCat = new HashMap<>();
        Map<String, Double> gananciasPorCat = new HashMap<>();

        List<VentaResponse> ventasDetalladas = new java.util.ArrayList<>();
        for (VentaEntity v : ventas) {
            List<DetalleVentaEntity> detalles = detalleVentaRepository.findByVentaId(v.getId());
            
            // Agregar al historial detallado
            ventasDetalladas.add(ventaMapper.toResponse(v, ventaMapper.toDetalleResponseList(detalles)));

            for (DetalleVentaEntity d : detalles) {
                // Productos Favoritos
                String nombreProd = d.getProducto().getNombre();
                productosFavoritos.put(nombreProd, productosFavoritos.getOrDefault(nombreProd, 0L) + d.getCantidad());

                // Ventas por Categoría
                String nombreCat = (d.getProducto().getCategoria() != null) ? d.getProducto().getCategoria().getNombre() : "Sin Categoría";
                double subtotal = d.getSubtotalItem() != null ? d.getSubtotalItem() : (d.getPrecioUnitario() * d.getCantidad());
                double margenDetalle = d.getMargenItem() != null ? d.getMargenItem() : ((d.getPrecioUnitario() - d.getCostoUnitario()) * d.getCantidad());

                ventasPorCat.put(nombreCat, ventasPorCat.getOrDefault(nombreCat, 0.0) + subtotal);
                gananciasPorCat.put(nombreCat, gananciasPorCat.getOrDefault(nombreCat, 0.0) + margenDetalle);
            }
        }
        
        ventasDetalladas.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        dashboard.setDetalleVentas(ventasDetalladas);
        
        dashboard.setProductosMasVendidos(productosFavoritos);
        dashboard.setVentasPorCategoria(ventasPorCat);
        dashboard.setGananciasPorCategoria(gananciasPorCat);

        // Gastos por Categoría
        Map<String, Double> gastosPorCat = gastos.stream()
                .collect(Collectors.groupingBy(
                        g -> (g.getCategoriaGasto() != null) ? g.getCategoriaGasto().getNombre() : "Sin Categoría",
                        Collectors.summingDouble(GastoEntity::getMonto)
                ));
        dashboard.setGastosPorCategoria(gastosPorCat);
        
        // Gastos Desglosados Jerárquicos: Categoría -> { SubItem -> Monto }
        Map<String, Map<String, Double>> gastosJerarquicos = new HashMap<>();
        
        // Detalle Unificado de Egresos para la tabla individual
        List<GastoResponse> egresosDetallados = new java.util.ArrayList<>();

        // 1. Procesar Gastos normales
        for (GastoEntity g : gastos) {
            String catNombre = (g.getCategoriaGasto() != null) ? g.getCategoriaGasto().getNombre() : "Otros";
            String vendedor = (g.getSesion() != null && g.getSesion().getUsuario() != null) ? g.getSesion().getUsuario().getNombre() : "Admin";
            String subItem;

            if (catNombre.equalsIgnoreCase("Nómina") || catNombre.equalsIgnoreCase("Nomina") || catNombre.equalsIgnoreCase("Turno")) {
                subItem = vendedor;
            } else if (g.getSubcategoriaGasto() != null) {
                subItem = g.getSubcategoriaGasto().getNombre();
            } else {
                subItem = g.getDescripcion() != null ? g.getDescripcion() : "General";
            }
            
            gastosJerarquicos.computeIfAbsent(catNombre, k -> new HashMap<>());
            Map<String, Double> subMap = gastosJerarquicos.get(catNombre);
            subMap.put(subItem, subMap.getOrDefault(subItem, 0.0) + g.getMonto());
            
            egresosDetallados.add(gastoMapper.toResponse(g));
        }

        // 2. Procesar Sueldos registrados (Módulo Nómina)
        for (SueldoPagadoEntity s : sueldos) {
            String catNombre = "Nómina";
            String subItem = (s.getUsuario() != null) ? s.getUsuario().getNombre() : "Desconocido";
            
            gastosJerarquicos.computeIfAbsent(catNombre, k -> new HashMap<>());
            Map<String, Double> subMap = gastosJerarquicos.get(catNombre);
            subMap.put(subItem, subMap.getOrDefault(subItem, 0.0) + s.getMonto());
            
            GastoResponse gr = new GastoResponse();
            gr.setId(s.getId());
            gr.setCategoriaGastoNombre("Nómina");
            gr.setDescripcion("Pago de Sueldo / Turno");
            gr.setMonto(s.getMonto());
            gr.setNombreUsuario(subItem);
            gr.setCreatedAt(s.getCreatedAt());
            gr.setFechaRegistroManual(s.getCreatedAt()); // Set as manual to prioritize it
            egresosDetallados.add(gr);
        }

        dashboard.setGastosDesglosados(gastosJerarquicos);
        
        // Ordenar egresos por fecha descendente
        egresosDetallados.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        dashboard.setDetalleGastos(egresosDetallados);

        return dashboard;
    }

    @Override
    public EstadisticasInversionResponse obtenerEstadisticasInversion(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59, 999_000_000);

        List<GastoEntity> gastosPeriodo = filtrarGastosOperativos(
                gastoRepository.findByRangoFechas(inicioDateTime, finDateTime));

        double gastosOperativos = sumarGastos(gastosPeriodo);

        // Compras registradas: fuente unica el modulo Compras (incluye migradas sin detalle).
        // Entradas iniciales: altas de producto con stock que no vienen de una compra.
        double comprasRegistradasPeriodo = valor(compraMercanciaRepository.sumComprasEntre(inicio, fin));
        double entradasInicialesPeriodo = valor(movimientoInventarioRepository
                .sumEntradasSinCompraEntre(TipoMovimiento.ENTRADA, inicioDateTime, finDateTime));
        double inversionPeriodo = comprasRegistradasPeriodo + entradasInicialesPeriodo;

        LocalDate corte = saldoInicialMercanciaRepository.findFechaCorte();
        double saldoInicial = (corte == null || fin.isBefore(corte))
                ? 0.0
                : valor(saldoInicialMercanciaRepository.sumMontoBase());

        double comprasRegistradasHasta = valor(compraMercanciaRepository.sumComprasHasta(fin));
        double entradasInicialesHasta = valor(movimientoInventarioRepository
                .sumEntradasSinCompraHasta(TipoMovimiento.ENTRADA, finDateTime));
        double inversionHastaFin = saldoInicial + comprasRegistradasHasta + entradasInicialesHasta;

        double gastosAcumulados = sumarGastos(filtrarGastosOperativos(gastoRepository.findAll()));
        double inversionAcumulada = valor(saldoInicialMercanciaRepository.sumMontoBase())
                + valor(compraMercanciaRepository.sumCompras())
                + valor(movimientoInventarioRepository.sumEntradasSinCompra(TipoMovimiento.ENTRADA));

        EstadisticasInversionResponse response = new EstadisticasInversionResponse();
        response.setGastosOperativos(redondear(gastosOperativos));
        response.setComprasRegistradas(redondear(comprasRegistradasPeriodo));
        response.setEntradasIniciales(redondear(entradasInicialesPeriodo));
        response.setInversionMercanciaPeriodo(redondear(inversionPeriodo));
        response.setSaldoInicialMercancia(redondear(saldoInicial));
        response.setInversionMercanciaTotal(redondear(inversionHastaFin));
        response.setTotalInvertidoPeriodo(redondear(gastosOperativos + inversionPeriodo));
        response.setTotalInvertidoAcumulado(redondear(gastosAcumulados + inversionAcumulada));
        response.setValorInventarioActual(redondear(valor(productoRepository.sumValorInventarioActual())));
        response.setValorInventarioVenta(redondear(valor(productoRepository.sumValorInventarioVenta())));
        response.setUnidadesInventarioActual(valorLong(productoRepository.sumUnidadesInventarioActual()));
        response.setProductosConStock(valorLong(productoRepository.countProductosConStock()).intValue());
        response.setGastosOperativosPorSubcategoria(agruparGastosPorSubcategoria(gastosPeriodo));
        response.setGastosOperativosDetalle(gastoMapper.toResponseList(ordenarGastosPorFechaDesc(gastosPeriodo)));
        response.setComprasPorProducto(movimientoInventarioRepository
                .findComprasPorProductoEntre(TipoMovimiento.ENTRADA, inicioDateTime, finDateTime));
        response.setEntradasInicialesPorProducto(movimientoInventarioRepository
                .findEntradasSinCompraPorProductoEntre(TipoMovimiento.ENTRADA, inicioDateTime, finDateTime));
        response.setProductosConSaldoInicial((int) saldoInicialMercanciaRepository.count());
        return response;
    }

    @Override
    public EstadisticasInversionResponse obtenerEstadisticasInversionGlobal() {
        List<GastoEntity> gastos = filtrarGastosOperativos(gastoRepository.findAll());

        double gastosOperativos = sumarGastos(gastos);
        double saldoInicial = valor(saldoInicialMercanciaRepository.sumMontoBase());
        double comprasRegistradas = valor(compraMercanciaRepository.sumCompras());
        double entradasIniciales = valor(movimientoInventarioRepository
                .sumEntradasSinCompra(TipoMovimiento.ENTRADA));
        double compras = comprasRegistradas + entradasIniciales;

        EstadisticasInversionResponse response = new EstadisticasInversionResponse();
        response.setGastosOperativos(redondear(gastosOperativos));
        response.setComprasRegistradas(redondear(comprasRegistradas));
        response.setEntradasIniciales(redondear(entradasIniciales));
        response.setInversionMercanciaPeriodo(redondear(compras));
        response.setSaldoInicialMercancia(redondear(saldoInicial));
        response.setInversionMercanciaTotal(redondear(saldoInicial + compras));
        response.setTotalInvertidoPeriodo(redondear(gastosOperativos + compras));
        response.setTotalInvertidoAcumulado(redondear(saldoInicial + gastosOperativos + compras));
        response.setValorInventarioActual(redondear(valor(productoRepository.sumValorInventarioActual())));
        response.setValorInventarioVenta(redondear(valor(productoRepository.sumValorInventarioVenta())));
        response.setUnidadesInventarioActual(valorLong(productoRepository.sumUnidadesInventarioActual()));
        response.setProductosConStock(valorLong(productoRepository.countProductosConStock()).intValue());
        response.setGastosOperativosPorSubcategoria(agruparGastosPorSubcategoria(gastos));
        response.setGastosOperativosDetalle(gastoMapper.toResponseList(ordenarGastosPorFechaDesc(gastos)));
        response.setComprasPorProducto(movimientoInventarioRepository.findComprasPorProducto(TipoMovimiento.ENTRADA));
        response.setEntradasInicialesPorProducto(movimientoInventarioRepository
                .findEntradasSinCompraPorProducto(TipoMovimiento.ENTRADA));
        response.setProductosConSaldoInicial((int) saldoInicialMercanciaRepository.count());
        return response;
    }

    private List<GastoEntity> filtrarGastosOperativos(List<GastoEntity> gastos) {
        return gastos.stream()
                .filter(this::esGastoOperativo)
                .toList();
    }

    private boolean esGastoOperativo(GastoEntity gasto) {
        return gasto.getCategoriaGasto() != null
                && CATEGORIA_GASTOS_OPERATIVOS.equalsIgnoreCase(normalizar(gasto.getCategoriaGasto().getNombre()));
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim();
    }

    private List<GastoEntity> ordenarGastosPorFechaDesc(List<GastoEntity> gastos) {
        return gastos.stream()
                .sorted((a, b) -> {
                    LocalDateTime fa = a.getFechaRegistroManual() != null ? a.getFechaRegistroManual() : a.getCreatedAt();
                    LocalDateTime fb = b.getFechaRegistroManual() != null ? b.getFechaRegistroManual() : b.getCreatedAt();
                    if (fa == null && fb == null) {
                        return 0;
                    }
                    if (fa == null) {
                        return 1;
                    }
                    if (fb == null) {
                        return -1;
                    }
                    return fb.compareTo(fa);
                })
                .toList();
    }

    private double sumarGastos(List<GastoEntity> gastos) {
        return gastos.stream().mapToDouble(GastoEntity::getMonto).sum();
    }

    private boolean esEfectivo(String nombre) {
        return nombre != null && nombre.toLowerCase(Locale.ROOT).contains("efectivo");
    }

    private double valor(Double valor) {
        return valor != null ? valor : 0.0;
    }

    private Long valorLong(Long valor) {
        return valor != null ? valor : 0L;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    private Map<String, Double> agruparGastosPorSubcategoria(List<GastoEntity> gastos) {
        return gastos.stream().collect(Collectors.groupingBy(
                g -> (g.getSubcategoriaGasto() != null) ? g.getSubcategoriaGasto().getNombre() : "Sin subcategoría",
                Collectors.summingDouble(GastoEntity::getMonto)
        ));
    }
}
