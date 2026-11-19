package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.CompraMercanciaRequest;
import com.chancla.chancla_lite_auth.dto.response.CompraMercanciaResponse;

import java.util.List;

public interface CompraMercanciaService {

    List<CompraMercanciaResponse> listar();

    CompraMercanciaResponse obtenerPorId(Long id);

    CompraMercanciaResponse crear(CompraMercanciaRequest request);
}
