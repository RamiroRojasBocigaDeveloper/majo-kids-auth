package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;

public class GastoResponse {

    private Long id;
    private Long sesionId;
    private Integer categoriaGastoId;
    private String categoriaGastoNombre;
    private Long subcategoriaGastoId;
    private String subcategoriaGastoNombre;
    private String descripcion;
    private Double monto;
    private LocalDateTime createdAt;
    private LocalDateTime fechaRegistroManual;
    private String nombreUsuario;

    public GastoResponse() {
    }

    // Getters y Setters
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public LocalDateTime getFechaRegistroManual() { return fechaRegistroManual; }
    public void setFechaRegistroManual(LocalDateTime fechaRegistroManual) { this.fechaRegistroManual = fechaRegistroManual; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public Integer getCategoriaGastoId() { return categoriaGastoId; }
    public void setCategoriaGastoId(Integer categoriaGastoId) { this.categoriaGastoId = categoriaGastoId; }

    public String getCategoriaGastoNombre() { return categoriaGastoNombre; }
    public void setCategoriaGastoNombre(String categoriaGastoNombre) { this.categoriaGastoNombre = categoriaGastoNombre; }

    public Long getSubcategoriaGastoId() { return subcategoriaGastoId; }
    public void setSubcategoriaGastoId(Long subcategoriaGastoId) { this.subcategoriaGastoId = subcategoriaGastoId; }

    public String getSubcategoriaGastoNombre() { return subcategoriaGastoNombre; }
    public void setSubcategoriaGastoNombre(String subcategoriaGastoNombre) { this.subcategoriaGastoNombre = subcategoriaGastoNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
