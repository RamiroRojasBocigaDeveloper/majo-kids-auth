package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReporteComprasMercanciaIntegrationTest {

    @Autowired
    private ReporteService reporteService;

    @Autowired
    private MockMvc mockMvc;

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

        assertNotNull(d.getVentasEfectivo());
        assertNotNull(d.getVentasNoEfectivo());
        assertNotNull(d.getGastosEfectivo());
        assertNotNull(d.getGastosNoEfectivo());
        assertNotNull(d.getComprasEfectivo());
        assertNotNull(d.getComprasNoEfectivo());
        assertNotNull(d.getCajaNetaEfectivo());
        assertNotNull(d.getCajaNetaNoEfectivo());

        assertEquals(d.getTotalVentas(), d.getVentasEfectivo() + d.getVentasNoEfectivo(), 0.01);
        assertEquals(d.getTotalGastos(), d.getGastosEfectivo() + d.getGastosNoEfectivo(), 0.01);
        assertEquals(d.getComprasMercancia(), d.getComprasEfectivo() + d.getComprasNoEfectivo(), 0.01);
        assertEquals(d.getCapitalDisponible(), d.getCajaNetaEfectivo() + d.getCajaNetaNoEfectivo(), 0.01);
    }

    @Test
    void elResumenPorRangoIncluyeLasComprasDelPeriodo() {
        DashboardResponse global = reporteService.obtenerResumenGlobal();
        DashboardResponse rango = reporteService.obtenerResumenPorRangoFechas(
                LocalDate.of(1900, 1, 1), LocalDate.of(2999, 12, 31));

        assertEquals(global.getComprasMercancia(), rango.getComprasMercancia(), 0.01);
        assertEquals(global.getCapitalDisponible(), rango.getCapitalDisponible(), 0.01);
        assertNotNull(rango.getCajaNetaEfectivo());
        assertNotNull(rango.getCajaNetaNoEfectivo());
        assertEquals(rango.getCapitalDisponible(), rango.getCajaNetaEfectivo() + rango.getCajaNetaNoEfectivo(), 0.01);
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void elEndpointGlobalExponeElDesgloseDeCaja() throws Exception {
        mockMvc.perform(get("/api/reportes/dashboard/global"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capitalDisponible").isNumber())
                .andExpect(jsonPath("$.ventasEfectivo").isNumber())
                .andExpect(jsonPath("$.ventasNoEfectivo").isNumber())
                .andExpect(jsonPath("$.gastosEfectivo").isNumber())
                .andExpect(jsonPath("$.gastosNoEfectivo").isNumber())
                .andExpect(jsonPath("$.comprasEfectivo").isNumber())
                .andExpect(jsonPath("$.comprasNoEfectivo").isNumber())
                .andExpect(jsonPath("$.cajaNetaEfectivo").isNumber())
                .andExpect(jsonPath("$.cajaNetaNoEfectivo").isNumber());
    }
}
