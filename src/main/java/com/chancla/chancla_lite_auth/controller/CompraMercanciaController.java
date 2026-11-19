package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.CompraMercanciaRequest;
import com.chancla.chancla_lite_auth.dto.response.CompraMercanciaResponse;
import com.chancla.chancla_lite_auth.service.CompraMercanciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/compras-mercancia")
public class CompraMercanciaController {

    private final CompraMercanciaService compraMercanciaService;

    public CompraMercanciaController(CompraMercanciaService compraMercanciaService) {
        this.compraMercanciaService = compraMercanciaService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<CompraMercanciaResponse>> listar() {
        return ResponseEntity.ok(compraMercanciaService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CompraMercanciaResponse> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(compraMercanciaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@Valid @RequestBody CompraMercanciaRequest request) {
        try {
            CompraMercanciaResponse response = compraMercanciaService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
