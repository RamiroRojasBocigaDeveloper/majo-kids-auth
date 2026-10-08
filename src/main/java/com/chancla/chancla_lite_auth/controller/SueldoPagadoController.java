package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.SueldoPagadoRequest;
import com.chancla.chancla_lite_auth.dto.response.SueldoPagadoResponse;
import com.chancla.chancla_lite_auth.service.SueldoPagadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sueldos-pagados")
public class SueldoPagadoController {

    private final SueldoPagadoService sueldoPagadoService;

    @Autowired
    public SueldoPagadoController(SueldoPagadoService sueldoPagadoService) {
        this.sueldoPagadoService = sueldoPagadoService;
    }

    @GetMapping
    public ResponseEntity<List<SueldoPagadoResponse>> obtenerTodos() {
        return ResponseEntity.ok(sueldoPagadoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SueldoPagadoResponse> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(sueldoPagadoService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SueldoPagadoResponse>> obtenerPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(sueldoPagadoService.obtenerPorUsuario(usuarioId));
    }

    @GetMapping("/sesion/{sesionId}")
    public ResponseEntity<List<SueldoPagadoResponse>> obtenerPorSesion(@PathVariable Long sesionId) {
        return ResponseEntity.ok(sueldoPagadoService.obtenerPorSesion(sesionId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> registrarPago(@Valid @RequestBody SueldoPagadoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(sueldoPagadoService.registrarPago(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/manual")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasRole('VENDEDOR')")
    public ResponseEntity<?> registrarSueldoManual(@Valid @RequestBody SueldoPagadoRequest request) {
        try {
            String resultado = sueldoPagadoService.registrarSueldoManual(
                    request.getUsuarioId(), request.getSesionId(), request.getMonto()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        try {
            sueldoPagadoService.eliminarPago(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
