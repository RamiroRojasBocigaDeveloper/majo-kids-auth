package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final VentaService ventaService;

    @Autowired
    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @PostMapping
    public ResponseEntity<?> procesarVenta(@Valid @RequestBody VentaRequest request) {
        try {
            VentaResponse response = ventaService.procesarVenta(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarVenta(@PathVariable Long id, @Valid @RequestBody VentaRequest request) {
        try {
            VentaResponse response = ventaService.actualizarVenta(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaResponse> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ventaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/factura/{numeroFactura}")
    public ResponseEntity<VentaResponse> obtenerPorFactura(@PathVariable String numeroFactura) {
        try {
            return ResponseEntity.ok(ventaService.obtenerPorNumeroFactura(numeroFactura));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<VentaResponse>> obtenerTodas() {
        return ResponseEntity.ok(ventaService.obtenerTodas());
    }

    @GetMapping("/sesion/{sesionId}")
    public ResponseEntity<List<VentaResponse>> obtenerPorSesion(@PathVariable Long sesionId) {
        return ResponseEntity.ok(ventaService.obtenerPorSesion(sesionId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<VentaResponse>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(ventaService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/rango")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<VentaResponse>> obtenerPorRango(
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime inicio,
            @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) java.time.LocalDateTime fin) {
        return ResponseEntity.ok(ventaService.obtenerPorRango(inicio, fin));
    }
}
