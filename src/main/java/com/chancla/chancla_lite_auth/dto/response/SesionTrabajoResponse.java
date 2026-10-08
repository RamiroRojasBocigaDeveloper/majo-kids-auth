package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class SesionTrabajoResponse {

    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private String rolUsuario;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private String estado;
    private LocalDateTime createdAt;
    private List<ProductoResponse> alertasStock;

    public SesionTrabajoResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

    public String getRolUsuario() { return rolUsuario; }
    public void setRolUsuario(String rolUsuario) { this.rolUsuario = rolUsuario; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<ProductoResponse> getAlertasStock() { return alertasStock; }
    public void setAlertasStock(List<ProductoResponse> alertasStock) { this.alertasStock = alertasStock; }
}
