package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
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

    @Test
    void consultaGlobalEjecutaLasConsultasDeInversion() {
        EstadisticasInversionResponse response = reporteService.obtenerEstadisticasInversionGlobal();

        assertNotNull(response);
        assertNotNull(response.getSaldoInicialMercancia());
        assertNotNull(response.getComprasPorProducto());
        assertNotNull(response.getGastosOperativosPorSubcategoria());
        assertNotNull(response.getTotalInvertidoAcumulado());
        assertTrue(movimientoInventarioRepository.sumCompras(TipoMovimiento.ENTRADA) >= 0);
    }

    @Test
    void gastosOperativosSoloIncluyenLaCategoriaLogisticaParaSurtir() {
        EstadisticasInversionResponse response = reporteService.obtenerEstadisticasInversionGlobal();

        double esperado = gastoRepository.findAll().stream()
                .filter(g -> g.getCategoriaGasto() != null
                        && CATEGORIA_OPERATIVA.equalsIgnoreCase(g.getCategoriaGasto().getNombre()))
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
        assertNotNull(response.getComprasPorProducto());
        assertNotNull(response.getGastosOperativosPorSubcategoria());
    }
}
