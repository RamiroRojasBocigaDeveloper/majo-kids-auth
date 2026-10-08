package com.chancla.chancla_lite_auth.dto.response;

public class EstadoStockDTO {
    private Long id;
    private String nombre;
    private String referencia;
    private String categoria;
    private Integer stockActual;
    private Integer stockMinimo;
    private Double precioCompra;
    private Double precioVenta;
    private String estado;

    // Constructor, getters y setters
    public EstadoStockDTO() {}

    public EstadoStockDTO(Long id, String nombre, String referencia, String categoria,
                         Integer stockActual, Integer stockMinimo, Double precioCompra,
                         Double precioVenta, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.referencia = referencia;
        this.categoria = categoria;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.estado = estado;
    }

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public Double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(Double precioCompra) { this.precioCompra = precioCompra; }

    public Double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(Double precioVenta) { this.precioVenta = precioVenta; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}