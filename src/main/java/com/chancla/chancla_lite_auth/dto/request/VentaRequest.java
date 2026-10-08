package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;

public class VentaRequest {

    private Long sesionId;

    @NotNull(message = "El método de pago es obligatorio")
    private Integer metodoPagoId;

    private Double descuento = 0.0;

    @NotNull(message = "La lista de detalles no puede estar vacía")
    private List<DetalleVentaRequest> detalles;

    public VentaRequest() {
    }

    private LocalDate fechaHistorica;
    private Long usuarioId;

    // Getters y Setters
    public LocalDate getFechaHistorica() { return fechaHistorica; }
    public void setFechaHistorica(LocalDate fechaHistorica) { this.fechaHistorica = fechaHistorica; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public Integer getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(Integer metodoPagoId) { this.metodoPagoId = metodoPagoId; }

    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }

    public List<DetalleVentaRequest> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequest> detalles) { this.detalles = detalles; }

    public static class DetalleVentaRequest {
        @NotNull(message = "El producto es obligatorio")
        private Long productoId;

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor a cero")
        private Integer cantidad;

        private Double precioUnitario;

        public DetalleVentaRequest() {
        }

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

        public Double getPrecioUnitario() { return precioUnitario; }
        public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    }
}
