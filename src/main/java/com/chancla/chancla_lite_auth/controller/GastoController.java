package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.GastoRequest;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.service.GastoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    private final GastoService gastoService;

    @Autowired
    public GastoController(GastoService gastoService) {
        this.gastoService = gastoService;
    }

    @GetMapping
    public ResponseEntity<List<GastoResponse>> obtenerTodos() {
        return ResponseEntity.ok(gastoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoResponse> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(gastoService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/sesion/{sesionId}")
    public ResponseEntity<List<GastoResponse>> obtenerPorSesion(@PathVariable Long sesionId) {
        return ResponseEntity.ok(gastoService.obtenerPorSesion(sesionId));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<GastoResponse>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(gastoService.obtenerPorCategoria(categoriaId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@Valid @RequestBody GastoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(gastoService.crear(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody GastoRequest request) {
        try {
            return ResponseEntity.ok(gastoService.actualizar(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            gastoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
