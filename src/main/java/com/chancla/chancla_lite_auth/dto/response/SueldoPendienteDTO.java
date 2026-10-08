package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDate;

public class SueldoPendienteDTO {
    private Long sesionId;
    private LocalDate fecha;
    private Long usuarioId;
    private String usuario;
    private String rol;
    private Double sueldoDiario;
    private String estadoSesion;
    private String estadoPago;

    // Constructor, getters y setters
    public SueldoPendienteDTO() {}

    public SueldoPendienteDTO(Long sesionId, LocalDate fecha, Long usuarioId,
                             String usuario, String rol, Double sueldoDiario,
                             String estadoSesion, String estadoPago) {
        this.sesionId = sesionId;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.usuario = usuario;
        this.rol = rol;
        this.sueldoDiario = sueldoDiario;
        this.estadoSesion = estadoSesion;
        this.estadoPago = estadoPago;
    }

    // Getters y setters
    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Double getSueldoDiario() { return sueldoDiario; }
    public void setSueldoDiario(Double sueldoDiario) { this.sueldoDiario = sueldoDiario; }

    public String getEstadoSesion() { return estadoSesion; }
    public void setEstadoSesion(String estadoSesion) { this.estadoSesion = estadoSesion; }

    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
}