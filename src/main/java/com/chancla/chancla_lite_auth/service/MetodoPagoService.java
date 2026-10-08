package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.MetodoPagoRequest;
import com.chancla.chancla_lite_auth.dto.response.MetodoPagoResponse;
import java.util.List;

public interface MetodoPagoService {

    // Obtener todos los métodos de pago
    List<MetodoPagoResponse> obtenerTodos();

    // Obtener por ID
    MetodoPagoResponse obtenerPorId(Integer id);

    // Obtener por nombre
    MetodoPagoResponse obtenerPorNombre(String nombre);

    // Crear nuevo
    MetodoPagoResponse crear(MetodoPagoRequest request);

    // Actualizar existente
    MetodoPagoResponse actualizar(Integer id, MetodoPagoRequest request);

    // Eliminar
    void eliminar(Integer id);

    // Verificar si existe por nombre
    boolean existePorNombre(String nombre);

    // Buscar por nombre (coincidencia parcial)
    List<MetodoPagoResponse> buscarPorNombre(String nombre);
}
