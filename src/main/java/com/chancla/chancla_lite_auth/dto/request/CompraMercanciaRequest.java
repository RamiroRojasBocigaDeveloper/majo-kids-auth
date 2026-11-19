package com.chancla.chancla_lite_auth.dto.request;

import com.chancla.chancla_lite_auth.enums.MetodoPagoCompra;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class CompraMercanciaRequest {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Size(max = 150, message = "El proveedor no puede exceder 150 caracteres")
    private String proveedor;

    @Size(max = 200, message = "La descripción no puede exceder 200 caracteres")
    private String descripcion;

    @NotNull(message = "El método de pago es obligatorio")
    private MetodoPagoCompra metodoPago;

    @NotEmpty(message = "La compra debe tener al menos un producto")
    @Valid
    private List<DetalleCompraRequest> lineas;

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getProveedor() { return proveedor; }
    public void setProveedor(String proveedor) { this.proveedor = proveedor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public MetodoPagoCompra getMetodoPago() { return metodoPago; }
    public void setMetodoPago(MetodoPagoCompra metodoPago) { this.metodoPago = metodoPago; }

    public List<DetalleCompraRequest> getLineas() { return lineas; }
    public void setLineas(List<DetalleCompraRequest> lineas) { this.lineas = lineas; }
}
