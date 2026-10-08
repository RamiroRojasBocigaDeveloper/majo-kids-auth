package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class MovimientoInventarioRequest {

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "El tipo de movimiento es obligatorio (ENTRADA, SALIDA, AJUSTE)")
    private String tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotBlank(message = "El motivo es obligatorio")
    @Size(max = 100, message = "El motivo no puede exceder 100 caracteres")
    private String motivo;

    private Long referenciaId;

    private Double nuevoPrecioCompra;
    private Double nuevoPrecioVenta;

    public MovimientoInventarioRequest() {
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public Long getReferenciaId() { return referenciaId; }
    public void setReferenciaId(Long referenciaId) { this.referenciaId = referenciaId; }

    public Double getNuevoPrecioCompra() { return nuevoPrecioCompra; }
    public void setNuevoPrecioCompra(Double nuevoPrecioCompra) { this.nuevoPrecioCompra = nuevoPrecioCompra; }

    public Double getNuevoPrecioVenta() { return nuevoPrecioVenta; }
    public void setNuevoPrecioVenta(Double nuevoPrecioVenta) { this.nuevoPrecioVenta = nuevoPrecioVenta; }
}
