package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import com.chancla.chancla_lite_auth.service.MovimientoInventarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class MovimientoInventarioController {

    private final MovimientoInventarioService inventarioService;

    @Autowired
    public MovimientoInventarioController(MovimientoInventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @PostMapping("/movimiento")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> registrarMovimiento(@Valid @RequestBody MovimientoInventarioRequest request) {
        try {
            MovimientoInventarioResponse response = inventarioService.registrarMovimiento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/historial")
    public ResponseEntity<List<MovimientoInventarioResponse>> obtenerTodoElHistorial() {
        return ResponseEntity.ok(inventarioService.obtenerTodoElHistorial());
    }

    @GetMapping("/historial/producto/{productoId}")
    public ResponseEntity<List<MovimientoInventarioResponse>> obtenerHistorialPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.obtenerHistorialPorProducto(productoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoInventarioResponse> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(inventarioService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
