package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import com.chancla.chancla_lite_auth.repository.DetalleVentaRepository;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.SueldoPagadoRepository;
import com.chancla.chancla_lite_auth.repository.VentaRepository;
import com.chancla.chancla_lite_auth.mapper.GastoMapper;
import com.chancla.chancla_lite_auth.mapper.VentaMapper;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final SueldoPagadoRepository sueldoPagadoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final GastoMapper gastoMapper;
    private final VentaMapper ventaMapper;

    @Autowired
    public ReporteServiceImpl(VentaRepository ventaRepository,
                              GastoRepository gastoRepository,
                              SueldoPagadoRepository sueldoPagadoRepository,
                              DetalleVentaRepository detalleVentaRepository,
                              GastoMapper gastoMapper,
                              VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.gastoRepository = gastoRepository;
        this.sueldoPagadoRepository = sueldoPagadoRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.gastoMapper = gastoMapper;
        this.ventaMapper = ventaMapper;
    }

    @Override
    public DashboardResponse obtenerResumenPorSesion(Long sesionId) {
        List<VentaEntity> ventas = ventaRepository.findBySesionId(sesionId);
        List<GastoEntity> gastos = gastoRepository.findBySesionId(sesionId);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findBySesionId(sesionId);

        return construirDashboard(ventas, gastos, sueldos);
    }

    @Override
    public DashboardResponse obtenerResumenPorRangoFechas(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<VentaEntity> ventas = ventaRepository.findByRangoFechas(inicioDateTime, finDateTime);
        List<GastoEntity> gastos = gastoRepository.findByRangoFechas(inicioDateTime, finDateTime);
        List<SueldoPagadoEntity> sueldos = sueldoPagadoRepository.findByFechaPagoBetween(inicio, fin);

        return construirDashboard(ventas, gastos, sueldos);
    }

    @Override
    public DashboardResponse obtenerResumenGlobal() {
        return construirDashboard(ventaRepository.findAll(), gastoRepository.findAll(), sueldoPagadoRepository.findAll());
    }

    private DashboardResponse construirDashboard(List<VentaEntity> ventas, List<GastoEntity> gastos, List<SueldoPagadoEntity> sueldos) {
        DashboardResponse dashboard = new DashboardResponse();
        
        double totalVentas = ventas.stream().mapToDouble(VentaEntity::getTotal).sum();
        double totalGastos = gastos.stream().mapToDouble(GastoEntity::getMonto).sum();
        double totalSueldos = sueldos.stream().mapToDouble(SueldoPagadoEntity::getMonto).sum();

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
        
        // Flujo de Caja (Efectivo en Caja) = Ventas Totales - (Gastos Operativos + Sueldos)
        dashboard.setGananciaNeta(totalVentas - (totalGastos + totalSueldos));
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
}
