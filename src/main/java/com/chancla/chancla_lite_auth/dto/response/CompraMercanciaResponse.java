package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CompraMercanciaResponse {

    private Long id;
    private LocalDate fecha;
    private String proveedor;
    private String descripcion;
    private String metodoPago;
    private Double montoTotal;
    private Boolean detallada;
    private String usuarioNombre;
    private LocalDateTime createdAt;
    private List<DetalleCompraResponse> lineas;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }

    public Boolean getDetallada() { return detallada; }
    public void setDetallada(Boolean detallada) { this.detallada = detallada; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<DetalleCompraResponse> getLineas() { return lineas; }
    public void setLineas(List<DetalleCompraResponse> lineas) { this.lineas = lineas; }
}
