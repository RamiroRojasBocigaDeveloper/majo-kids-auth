package com.chancla.chancla_lite_auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumenSesionResponse {
    private Long sesionId;
    private Double totalVentas;
    private Double totalGastos;
    private Double saldoNeto;
    private Map<String, Double> ventasPorMetodoPago;
    private Double totalEfectivo;
    private Double totalNoEfectivo;
    private Double efectivoEsperado;
}
