package com.chancla.chancla_lite_auth.service;

public interface AuditoriaService {
    void registrar(String usuario, String accion, String entidad, Long entidadId, String detalles);
}
