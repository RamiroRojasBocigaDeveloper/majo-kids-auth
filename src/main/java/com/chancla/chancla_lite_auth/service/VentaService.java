package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import java.util.List;

public interface VentaService {

    VentaResponse procesarVenta(VentaRequest request);
    VentaResponse actualizarVenta(Long id, VentaRequest request);
    VentaResponse obtenerPorId(Long id);
    VentaResponse obtenerPorNumeroFactura(String numeroFactura);
    List<VentaResponse> obtenerTodas();
    List<VentaResponse> obtenerPorSesion(Long sesionId);
    List<VentaResponse> obtenerPorUsuario(Long usuarioId);
    List<VentaResponse> obtenerPorRango(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
}
