package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ReporteComprasMercanciaIntegrationTest {

    @Autowired
    private ReporteService reporteService;

    @Test
    void elResumenGlobalMantieneLasFormulasDeUtilidadYCapital() {
        DashboardResponse d = reporteService.obtenerResumenGlobal();

        assertNotNull(d.getUtilidadNeta());
        assertNotNull(d.getCapitalDisponible());
        assertNotNull(d.getComprasMercancia());

        assertEquals(d.getGananciaProductos() - d.getTotalGastos() - d.getTotalSueldos(),
                d.getUtilidadNeta(), 0.01);
        assertEquals(d.getTotalVentas() - d.getTotalGastos() - d.getTotalSueldos() - d.getComprasMercancia(),
                d.getCapitalDisponible(), 0.01);
        assertEquals(d.getCapitalDisponible(), d.getGananciaNeta(), 0.01);
    }

    @Test
    void elResumenPorRangoIncluyeLasComprasDelPeriodo() {
        DashboardResponse global = reporteService.obtenerResumenGlobal();
        DashboardResponse rango = reporteService.obtenerResumenPorRangoFechas(
                LocalDate.of(1900, 1, 1), LocalDate.of(2999, 12, 31));

        assertEquals(global.getComprasMercancia(), rango.getComprasMercancia(), 0.01);
        assertEquals(global.getCapitalDisponible(), rango.getCapitalDisponible(), 0.01);
    }
}
