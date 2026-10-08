package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.UsuarioRequest;
import com.chancla.chancla_lite_auth.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    List<UsuarioResponse> obtenerTodos();
    UsuarioResponse obtenerPorId(Long id);
    UsuarioResponse crear(UsuarioRequest request);
    UsuarioResponse actualizar(Long id, UsuarioRequest request);
    void eliminar(Long id);
    UsuarioResponse cambiarEstado(Long id, Boolean activo);
    void resetearPassword(Long id, String nuevaPassword);
}
