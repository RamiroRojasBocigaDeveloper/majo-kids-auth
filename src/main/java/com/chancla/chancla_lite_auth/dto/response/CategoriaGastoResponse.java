package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;

public class CategoriaGastoResponse {

    private Integer id;
    private String nombre;
    private LocalDateTime createdAt;

    // Constructor vacío
    public CategoriaGastoResponse() {
    }

    // Constructor con campos
    public CategoriaGastoResponse(Integer id, String nombre, LocalDateTime createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.createdAt = createdAt;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}