package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class VentaResponse {

    private Long id;
    private String numeroFactura;
    private Long sesionId;
    private Integer metodoPagoId;
    private String metodoPagoNombre;
    private Double subtotal;
    private Double descuento;
    private Double total;
    private LocalDateTime createdAt;
    private LocalDateTime fechaRegistroManual;
    private String nombreVendedor;
    private List<DetalleVentaResponse> detalles;

    public VentaResponse() {
    }

    // Getters y Setters
    public String getNombreVendedor() { return nombreVendedor; }
    public void setNombreVendedor(String nombreVendedor) { this.nombreVendedor = nombreVendedor; }

    public LocalDateTime getFechaRegistroManual() { return fechaRegistroManual; }
    public void setFechaRegistroManual(LocalDateTime fechaRegistroManual) { this.fechaRegistroManual = fechaRegistroManual; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }

    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public Integer getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(Integer metodoPagoId) { this.metodoPagoId = metodoPagoId; }

    public String getMetodoPagoNombre() { return metodoPagoNombre; }
    public void setMetodoPagoNombre(String metodoPagoNombre) { this.metodoPagoNombre = metodoPagoNombre; }

    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<DetalleVentaResponse> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaResponse> detalles) { this.detalles = detalles; }

    public static class DetalleVentaResponse {
        private Long id;
        private Long productoId;
        private String productoNombre;
        private String productoReferencia;
        private String categoriaNombre;
        private Integer cantidad;
        private Double precioUnitario;
        private Double subtotalItem;

        public DetalleVentaResponse() {
        }

        // Getters y Setters
        public String getCategoriaNombre() { return categoriaNombre; }
        public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public String getProductoNombre() { return productoNombre; }
        public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

        public String getProductoReferencia() { return productoReferencia; }
        public void setProductoReferencia(String productoReferencia) { this.productoReferencia = productoReferencia; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

        public Double getSubtotalItem() { return subtotalItem; }
        public void setSubtotalItem(Double subtotalItem) { this.subtotalItem = subtotalItem; }
    }
}
