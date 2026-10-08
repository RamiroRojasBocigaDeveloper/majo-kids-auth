package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.CategoriaRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaResponse;
import java.util.List;

public interface CategoriaService {

    // Obtener todas las categorías
    List<CategoriaResponse> obtenerTodas();

    // Obtener categoría por ID
    CategoriaResponse obtenerPorId(Integer id);

    // Obtener categoría por nombre
    CategoriaResponse obtenerPorNombre(String nombre);

    // Crear nueva categoría
    CategoriaResponse crear(CategoriaRequest request);

    // Actualizar categoría existente
    CategoriaResponse actualizar(Integer id, CategoriaRequest request);

    // Eliminar categoría
    void eliminar(Integer id);

    // Verificar si existe por nombre
    boolean existePorNombre(String nombre);

    // Buscar categorías por nombre
    List<CategoriaResponse> buscarPorNombre(String nombre);
}
