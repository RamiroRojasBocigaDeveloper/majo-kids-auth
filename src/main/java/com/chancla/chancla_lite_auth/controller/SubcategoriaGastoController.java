package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.SubcategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.SubcategoriaGastoResponse;
import com.chancla.chancla_lite_auth.service.SubcategoriaGastoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/subcategorias-gastos")
public class SubcategoriaGastoController {

    private final SubcategoriaGastoService subcategoriaGastoService;

    @Autowired
    public SubcategoriaGastoController(SubcategoriaGastoService subcategoriaGastoService) {
        this.subcategoriaGastoService = subcategoriaGastoService;
    }

    @GetMapping
    public ResponseEntity<List<SubcategoriaGastoResponse>> obtenerTodas() {
        return ResponseEntity.ok(subcategoriaGastoService.obtenerTodas());
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<SubcategoriaGastoResponse>> obtenerPorCategoria(@PathVariable Integer categoriaId) {
        return ResponseEntity.ok(subcategoriaGastoService.obtenerPorCategoria(categoriaId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@Valid @RequestBody SubcategoriaGastoRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(subcategoriaGastoService.crear(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody SubcategoriaGastoRequest request) {
        try {
            return ResponseEntity.ok(subcategoriaGastoService.actualizar(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            subcategoriaGastoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
