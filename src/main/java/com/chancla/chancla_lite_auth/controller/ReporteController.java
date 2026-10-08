package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.response.DashboardResponse;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ReporteController {

    private final ReporteService reporteService;

    @Autowired
    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/dashboard/global")
    public ResponseEntity<DashboardResponse> obtenerGlobal() {
        return ResponseEntity.ok(reporteService.obtenerResumenGlobal());
    }

    @GetMapping("/dashboard/sesion/{sesionId}")
    public ResponseEntity<DashboardResponse> obtenerPorSesion(@PathVariable Long sesionId) {
        return ResponseEntity.ok(reporteService.obtenerResumenPorSesion(sesionId));
    }

    @GetMapping("/dashboard/rango")
    public ResponseEntity<DashboardResponse> obtenerPorRango(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(reporteService.obtenerResumenPorRangoFechas(inicio, fin));
    }
}
