package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class DetalleCompraRequest {

    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @NotNull(message = "El costo unitario es obligatorio")
    @Positive(message = "El costo unitario debe ser mayor a cero")
    private Double costoUnitario;

    @PositiveOrZero(message = "El precio de venta no puede ser negativo")
    private Double precioVenta;

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(Double costoUnitario) { this.costoUnitario = costoUnitario; }

    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }
}
