package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SubcategoriaGastoRequest {

    @NotNull(message = "La categoría de gasto es obligatoria")
    private Integer categoriaGastoId;

    @NotBlank(message = "El nombre de la subcategoría es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombre;

    public SubcategoriaGastoRequest() {
    }

    public Integer getCategoriaGastoId() { return categoriaGastoId; }
    public void setCategoriaGastoId(Integer categoriaGastoId) { this.categoriaGastoId = categoriaGastoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
