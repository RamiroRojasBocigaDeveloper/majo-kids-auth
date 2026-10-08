package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;

public class CategoriaResponse {

    private Integer id;
    private String nombre;
    private LocalDateTime createdAt;

    public CategoriaResponse() {
    }

    public CategoriaResponse(Integer id, String nombre, LocalDateTime createdAt) {
        this.id = id;
        this.nombre = nombre;
        this.createdAt = createdAt;
    }

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
