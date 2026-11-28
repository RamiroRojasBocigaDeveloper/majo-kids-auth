package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EstadisticasInversionServiceTest {

    private static final String CATEGORIA_OPERATIVA = "Logistica para surtir";

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void consultaGlobalEjecutaLasConsultasDeInversion() {
        EstadisticasInversionResponse response = reporteService.obtenerEstadisticasInversionGlobal();

        assertNotNull(response);
        assertNotNull(response.getSaldoInicialMercancia());
        assertNotNull(response.getComprasRegistradas());
        assertNotNull(response.getEntradasIniciales());
        assertNotNull(response.getComprasPorProducto());
        assertNotNull(response.getEntradasInicialesPorProducto());
        assertNotNull(response.getGastosOperativosPorSubcategoria());
        assertNotNull(response.getTotalInvertidoAcumulado());
        assertNotNull(response.getValorInventarioActual());
        assertNotNull(response.getValorInventarioVenta());
        assertNotNull(response.getGastosOperativosDetalle());
        assertNotNull(response.getUnidadesInventarioActual());
        assertNotNull(response.getProductosConStock());
        assertEquals(response.getComprasRegistradas() + response.getEntradasIniciales(),
                response.getInversionMercanciaPeriodo(), 0.01);
        assertTrue(movimientoInventarioRepository.sumCompras(TipoMovimiento.ENTRADA) >= 0);

        var productosConStock = productoRepository.findAll().stream()
                .filter(p -> p.getStockActual() != null && p.getStockActual() > 0)
                .toList();
        double inventarioEsperado = productosConStock.stream()
                .mapToDouble(p -> p.getStockActual() * p.getPrecioCompra())
                .sum();
        long unidadesEsperadas = productosConStock.stream()
                .mapToLong(ProductoEntity::getStockActual)
                .sum();

        double inventarioVentaEsperado = productosConStock.stream()
                .mapToDouble(p -> p.getStockActual() * p.getPrecioVenta())
                .sum();

        assertEquals(inventarioEsperado, response.getValorInventarioActual(), 0.01);
        assertEquals(inventarioVentaEsperado, response.getValorInventarioVenta(), 0.01);
        assertEquals(unidadesEsperadas, response.getUnidadesInventarioActual().longValue());
        assertEquals(productosConStock.size(), response.getProductosConStock().intValue());
    }

    @Test
    void gastosOperativosSoloIncluyenLaCategoriaLogisticaParaSurtir() {
        EstadisticasInversionResponse response = reporteService.obtenerEstadisticasInversionGlobal();

        double esperado = gastoRepository.findAll().stream()
                .filter(g -> g.getCategoriaGasto() != null
                        && CATEGORIA_OPERATIVA.equalsIgnoreCase(normalizar(g.getCategoriaGasto().getNombre())))
                .mapToDouble(g -> g.getMonto() != null ? g.getMonto() : 0.0)
                .sum();

        assertEquals(esperado, response.getGastosOperativos(), 0.01);
        assertEquals(
                response.getSaldoInicialMercancia()
                        + response.getGastosOperativos()
                        + response.getInversionMercanciaPeriodo(),
                response.getTotalInvertidoAcumulado(),
                0.01);
        assertEquals(
                response.getSaldoInicialMercancia() + response.getInversionMercanciaPeriodo(),
                response.getInversionMercanciaTotal(),
                0.01);
    }

    @Test
    void consultaPorRangoEjecutaLasConsultasDeInversion() {
        EstadisticasInversionResponse response = reporteService
                .obtenerEstadisticasInversion(LocalDate.now(), LocalDate.now());

        assertNotNull(response);
        assertNotNull(response.getSaldoInicialMercancia());
        assertNotNull(response.getInversionMercanciaPeriodo());
        assertNotNull(response.getComprasRegistradas());
        assertNotNull(response.getEntradasIniciales());
        assertNotNull(response.getComprasPorProducto());
        assertNotNull(response.getEntradasInicialesPorProducto());
        assertNotNull(response.getGastosOperativosPorSubcategoria());
        assertNotNull(response.getValorInventarioActual());
        assertNotNull(response.getUnidadesInventarioActual());
        assertNotNull(response.getProductosConStock());
        assertEquals(response.getComprasRegistradas() + response.getEntradasIniciales(),
                response.getInversionMercanciaPeriodo(), 0.01);
    }

    private static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim();
    }
}
