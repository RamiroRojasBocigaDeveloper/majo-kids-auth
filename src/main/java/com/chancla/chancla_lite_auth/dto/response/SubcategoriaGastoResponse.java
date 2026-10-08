package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;

public class SubcategoriaGastoResponse {

    private Long id;
    private Integer categoriaGastoId;
    private String categoriaGastoNombre;
    private String nombre;
    private LocalDateTime createdAt;

    public SubcategoriaGastoResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCategoriaGastoId() { return categoriaGastoId; }
    public void setCategoriaGastoId(Integer categoriaGastoId) { this.categoriaGastoId = categoriaGastoId; }

    public String getCategoriaGastoNombre() { return categoriaGastoNombre; }
    public void setCategoriaGastoNombre(String categoriaGastoNombre) { this.categoriaGastoNombre = categoriaGastoNombre; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
