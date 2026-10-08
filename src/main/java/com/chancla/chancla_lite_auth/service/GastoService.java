package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.GastoRequest;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import java.util.List;

public interface GastoService {

    List<GastoResponse> obtenerTodos();

    List<GastoResponse> obtenerPorSesion(Long sesionId);

    List<GastoResponse> obtenerPorCategoria(Integer categoriaId);

    GastoResponse crear(GastoRequest request);

    GastoResponse actualizar(Long id, GastoRequest request);

    void eliminar(Long id);

    GastoResponse obtenerPorId(Long id);
}
