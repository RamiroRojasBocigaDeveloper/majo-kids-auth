package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.SubcategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.SubcategoriaGastoResponse;
import java.util.List;

public interface SubcategoriaGastoService {

    List<SubcategoriaGastoResponse> obtenerTodas();

    List<SubcategoriaGastoResponse> obtenerPorCategoria(Integer categoriaGastoId);

    SubcategoriaGastoResponse crear(SubcategoriaGastoRequest request);

    SubcategoriaGastoResponse actualizar(Long id, SubcategoriaGastoRequest request);

    void eliminar(Long id);
}
