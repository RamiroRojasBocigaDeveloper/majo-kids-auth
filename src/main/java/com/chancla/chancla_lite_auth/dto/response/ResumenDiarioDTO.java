package com.chancla.chancla_lite_auth.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ResumenDiarioDTO {
    private Long sesionId;
    private LocalDate fecha;
    private String empleado;
    private String rol;
    private Double ventaBruta;
    private Double margenDia;
    private Double gastosDia;
    private Double resultadoNeto;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private Long totalVentas;

    // Constructor, getters y setters
    public ResumenDiarioDTO() {}

    public ResumenDiarioDTO(Long sesionId, LocalDate fecha, String empleado, String rol,
                           Double ventaBruta, Double margenDia, Double gastosDia,
                           Double resultadoNeto, LocalDateTime horaInicio, LocalDateTime horaFin,
                           Long totalVentas) {
        this.sesionId = sesionId;
        this.fecha = fecha;
        this.empleado = empleado;
        this.rol = rol;
        this.ventaBruta = ventaBruta;
        this.margenDia = margenDia;
        this.gastosDia = gastosDia;
        this.resultadoNeto = resultadoNeto;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.totalVentas = totalVentas;
    }

    // Getters y setters
    public Long getSesionId() { return sesionId; }
    public void setSesionId(Long sesionId) { this.sesionId = sesionId; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getEmpleado() { return empleado; }
    public void setEmpleado(String empleado) { this.empleado = empleado; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public Double getVentaBruta() { return ventaBruta; }
    public void setVentaBruta(Double ventaBruta) { this.ventaBruta = ventaBruta; }

    public Double getMargenDia() { return margenDia; }
    public void setMargenDia(Double margenDia) { this.margenDia = margenDia; }

    public Double getGastosDia() { return gastosDia; }
    public void setGastosDia(Double gastosDia) { this.gastosDia = gastosDia; }

    public Double getResultadoNeto() { return resultadoNeto; }
    public void setResultadoNeto(Double resultadoNeto) { this.resultadoNeto = resultadoNeto; }

    public LocalDateTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalDateTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalDateTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalDateTime horaFin) { this.horaFin = horaFin; }

    public Long getTotalVentas() { return totalVentas; }
    public void setTotalVentas(Long totalVentas) { this.totalVentas = totalVentas; }
}