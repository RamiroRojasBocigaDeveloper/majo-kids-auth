package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.CategoriaRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaResponse;
import com.chancla.chancla_lite_auth.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Autowired
    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> obtenerTodas() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> obtenerPorId(@PathVariable Integer id) {
        try {
            CategoriaResponse categoria = categoriaService.obtenerPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<CategoriaResponse> categorias = categoriaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','JEFE')")
    public ResponseEntity<?> crear(@Valid @RequestBody CategoriaRequest request) {
        try {
            CategoriaResponse nuevaCategoria = categoriaService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','JEFE')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @Valid @RequestBody CategoriaRequest request) {
        try {
            CategoriaResponse categoriaActualizada = categoriaService.actualizar(id, request);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','JEFE')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            categoriaService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        }
    }
}
