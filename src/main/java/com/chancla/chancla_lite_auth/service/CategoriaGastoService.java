package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.CategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaGastoResponse;
import java.util.List;

public interface CategoriaGastoService {

    // Obtener todas las categorías
    List<CategoriaGastoResponse> obtenerTodas();

    // Obtener categoría por ID
    CategoriaGastoResponse obtenerPorId(Integer id);

    // Obtener categoría por nombre
    CategoriaGastoResponse obtenerPorNombre(String nombre);

    // Crear nueva categoría
    CategoriaGastoResponse crear(CategoriaGastoRequest request);

    // Actualizar categoría existente
    CategoriaGastoResponse actualizar(Integer id, CategoriaGastoRequest request);

    // Eliminar categoría
    void eliminar(Integer id);

    // Verificar si existe por nombre
    boolean existePorNombre(String nombre);

    // Buscar categorías por nombre
    List<CategoriaGastoResponse> buscarPorNombre(String nombre);
}