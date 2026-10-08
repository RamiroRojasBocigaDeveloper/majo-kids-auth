package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import java.time.LocalDate;

public interface ReporteService {

    DashboardResponse obtenerResumenPorSesion(Long sesionId);

    DashboardResponse obtenerResumenPorRangoFechas(LocalDate inicio, LocalDate fin);

    DashboardResponse obtenerResumenGlobal();
}
