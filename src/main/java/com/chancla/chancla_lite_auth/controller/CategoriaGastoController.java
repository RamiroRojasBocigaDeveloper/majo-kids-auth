package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.CategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaGastoResponse;
import com.chancla.chancla_lite_auth.service.CategoriaGastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/categorias-gastos")
public class CategoriaGastoController {

    private final CategoriaGastoService categoriaGastoService;

    @Autowired
    public CategoriaGastoController(CategoriaGastoService categoriaGastoService) {
        this.categoriaGastoService = categoriaGastoService;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaGastoResponse>> obtenerTodas() {
        List<CategoriaGastoResponse> categorias = categoriaGastoService.obtenerTodas();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaGastoResponse> obtenerPorId(@PathVariable Integer id) {
        try {
            CategoriaGastoResponse categoria = categoriaGastoService.obtenerPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaGastoResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<CategoriaGastoResponse> categorias = categoriaGastoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@RequestBody CategoriaGastoRequest request) {
        try {
            CategoriaGastoResponse nuevaCategoria = categoriaGastoService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCategoria);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody CategoriaGastoRequest request) {
        try {
            CategoriaGastoResponse categoriaActualizada = categoriaGastoService.actualizar(id, request);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            categoriaGastoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
