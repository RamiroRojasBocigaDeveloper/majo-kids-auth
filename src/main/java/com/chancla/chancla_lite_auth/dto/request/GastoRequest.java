package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class GastoRequest {

    private Long sesionId;

    @NotNull(message = "La categoría de gasto es obligatoria")
    private Integer categoriaGastoId;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    private Long subcategoriaGastoId;

    public GastoRequest() {
    }

    private LocalDate fechaHistorica;
    private Long usuarioId;

    public LocalDate getFechaHistorica() { return fechaHistorica; }
    public void setFechaHistorica(LocalDate fechaHistorica) { this.fechaHistorica = fechaHistorica; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public Integer getCategoriaGastoId() { return categoriaGastoId; }
    public void setCategoriaGastoId(Integer categoriaGastoId) { this.categoriaGastoId = categoriaGastoId; }

    public Long getSubcategoriaGastoId() { return subcategoriaGastoId; }
    public void setSubcategoriaGastoId(Long subcategoriaGastoId) { this.subcategoriaGastoId = subcategoriaGastoId; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
