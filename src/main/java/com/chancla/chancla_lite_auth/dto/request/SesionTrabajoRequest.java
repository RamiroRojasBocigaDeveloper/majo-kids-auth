package com.chancla.chancla_lite_auth.dto.request;

import java.time.LocalDateTime;

public class SesionTrabajoRequest {

    private Long usuarioId;
    private LocalDateTime horaInicio;
    private String estado;

    public SesionTrabajoRequest() {
    }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
