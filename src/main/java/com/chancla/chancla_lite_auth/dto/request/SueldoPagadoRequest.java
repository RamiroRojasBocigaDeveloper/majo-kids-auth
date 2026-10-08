package com.chancla.chancla_lite_auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public class SueldoPagadoRequest {

    @NotNull(message = "El usuario (empleado) es obligatorio")
    private Long usuarioId;

    @NotNull(message = "La sesión de trabajo es obligatoria")
    private Long sesionId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotNull(message = "La fecha de pago es obligatoria")
    private LocalDate fechaPago;

    public SueldoPagadoRequest() {
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
}
