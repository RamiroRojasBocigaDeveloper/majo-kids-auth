package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.response.ResumenSesionResponse;
import com.chancla.chancla_lite_auth.dto.response.SesionTrabajoResponse;
import java.util.List;

public interface SesionTrabajoService {

    SesionTrabajoResponse abrirSesion(Long usuarioId);

    SesionTrabajoResponse cerrarSesion(Long sesionId);

    SesionTrabajoResponse obtenerSesionActiva(Long usuarioId);

    List<SesionTrabajoResponse> obtenerHistorialUsuario(Long usuarioId);

    List<SesionTrabajoResponse> obtenerTodas();

    SesionTrabajoResponse obtenerPorId(Long id);

    ResumenSesionResponse obtenerResumenCierre(Long sesionId);
}
