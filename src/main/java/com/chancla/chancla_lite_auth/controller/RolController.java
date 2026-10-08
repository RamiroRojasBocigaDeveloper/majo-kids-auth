package com.chancla.chancla_lite_auth.controller;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    @GetMapping
    public ResponseEntity<List<RolEntity>> obtenerTodos() {
        return ResponseEntity.ok(rolRepository.findAll());
    }
}
