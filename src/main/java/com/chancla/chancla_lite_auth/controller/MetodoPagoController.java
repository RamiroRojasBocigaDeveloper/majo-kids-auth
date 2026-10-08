package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.MetodoPagoRequest;
import com.chancla.chancla_lite_auth.dto.response.MetodoPagoResponse;
import com.chancla.chancla_lite_auth.service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/metodos-pago")
public class MetodoPagoController {

    private final MetodoPagoService metodoPagoService;

    @Autowired
    public MetodoPagoController(MetodoPagoService metodoPagoService) {
        this.metodoPagoService = metodoPagoService;
    }

    @GetMapping
    public ResponseEntity<List<MetodoPagoResponse>> obtenerTodos() {
        List<MetodoPagoResponse> metodos = metodoPagoService.obtenerTodos();
        return ResponseEntity.ok(metodos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetodoPagoResponse> obtenerPorId(@PathVariable Integer id) {
        try {
            MetodoPagoResponse metodo = metodoPagoService.obtenerPorId(id);
            return ResponseEntity.ok(metodo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<MetodoPagoResponse>> buscarPorNombre(@RequestParam String nombre) {
        List<MetodoPagoResponse> metodos = metodoPagoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(metodos);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> crear(@RequestBody MetodoPagoRequest request) {
        try {
            MetodoPagoResponse nuevoMetodo = metodoPagoService.crear(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMetodo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizar(@PathVariable Integer id, @RequestBody MetodoPagoRequest request) {
        try {
            MetodoPagoResponse metodoActualizado = metodoPagoService.actualizar(id, request);
            return ResponseEntity.ok(metodoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            metodoPagoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
