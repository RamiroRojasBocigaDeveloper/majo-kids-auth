package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.response.SesionTrabajoResponse;
import com.chancla.chancla_lite_auth.service.SesionTrabajoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sesiones")
public class SesionTrabajoController {

    private final SesionTrabajoService sesionTrabajoService;

    @Autowired
    public SesionTrabajoController(SesionTrabajoService sesionTrabajoService) {
        this.sesionTrabajoService = sesionTrabajoService;
    }

    @PostMapping("/abrir/{usuarioId}")
    public ResponseEntity<?> abrirSesion(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(sesionTrabajoService.abrirSesion(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cerrar/{sesionId}")
    public ResponseEntity<?> cerrarSesion(@PathVariable Long sesionId) {
        try {
            return ResponseEntity.ok(sesionTrabajoService.cerrarSesion(sesionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/activa/{usuarioId}")
    public ResponseEntity<?> obtenerSesionActiva(@PathVariable Long usuarioId) {
        try {
            return ResponseEntity.ok(sesionTrabajoService.obtenerSesionActiva(usuarioId));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SesionTrabajoResponse>> obtenerHistorial(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(sesionTrabajoService.obtenerHistorialUsuario(usuarioId));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<SesionTrabajoResponse>> obtenerTodas() {
        return ResponseEntity.ok(sesionTrabajoService.obtenerTodas());
    }

    @GetMapping("/{sesionId}/resumen")
    public ResponseEntity<?> obtenerResumenCierre(@PathVariable Long sesionId) {
        try {
            return ResponseEntity.ok(sesionTrabajoService.obtenerResumenCierre(sesionId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
