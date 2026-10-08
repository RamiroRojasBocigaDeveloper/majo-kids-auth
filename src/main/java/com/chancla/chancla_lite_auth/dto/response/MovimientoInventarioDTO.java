package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;

public class MovimientoInventarioDTO {
    private String referencia;
    private String producto;
    private String categoria;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private LocalDateTime fechaMovimiento;
    private Integer stockActual;
    private String referenciaMovimiento;

    // Constructor, getters y setters
    public MovimientoInventarioDTO() {}

    public MovimientoInventarioDTO(String referencia, String producto, String categoria,
                                  String tipo, Integer cantidad, String motivo,
                                  LocalDateTime fechaMovimiento, Integer stockActual,
                                  String referenciaMovimiento) {
        this.referencia = referencia;
        this.producto = producto;
        this.categoria = categoria;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.fechaMovimiento = fechaMovimiento;
        this.stockActual = stockActual;
        this.referenciaMovimiento = referenciaMovimiento;
    }

    // Getters y setters
    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public String getReferenciaMovimiento() { return referenciaMovimiento; }
    public void setReferenciaMovimiento(String referenciaMovimiento) { this.referenciaMovimiento = referenciaMovimiento; }
}