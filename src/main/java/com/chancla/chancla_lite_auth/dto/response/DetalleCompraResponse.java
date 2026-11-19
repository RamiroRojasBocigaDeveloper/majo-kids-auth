package com.chancla.chancla_lite_auth.dto.response;

public class DetalleCompraResponse {

    private Long productoId;
    private String productoNombre;
    private String referencia;
    private Integer cantidad;
    private Double costoUnitario;
    private Double subtotal;

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(Double costoUnitario) { this.costoUnitario = costoUnitario; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
}
