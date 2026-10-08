package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.SueldoPagadoRequest;
import com.chancla.chancla_lite_auth.dto.response.SueldoPagadoResponse;
import java.util.List;

public interface SueldoPagadoService {

    List<SueldoPagadoResponse> obtenerTodos();

    List<SueldoPagadoResponse> obtenerPorUsuario(Long usuarioId);

    List<SueldoPagadoResponse> obtenerPorSesion(Long sesionId);

    SueldoPagadoResponse registrarPago(SueldoPagadoRequest request);

    void eliminarPago(Long id);

    SueldoPagadoResponse obtenerPorId(Long id);

    String registrarSueldoManual(Long usuarioId, Long sesionId, Double monto);
}
