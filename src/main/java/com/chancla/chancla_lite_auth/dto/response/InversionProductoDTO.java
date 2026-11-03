package com.chancla.chancla_lite_auth.dto.response;

public class InversionProductoDTO {

    private Long productoId;
    private String referencia;
    private String nombre;
    private Long cantidad;
    private Double monto;

    public InversionProductoDTO() {
    }

    public InversionProductoDTO(Long productoId, String referencia, String nombre, Long cantidad, Double monto) {
        this.productoId = productoId;
        this.referencia = referencia;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.monto = monto;
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Long getCantidad() { return cantidad; }
    public void setCantidad(Long cantidad) { this.cantidad = cantidad; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
}
