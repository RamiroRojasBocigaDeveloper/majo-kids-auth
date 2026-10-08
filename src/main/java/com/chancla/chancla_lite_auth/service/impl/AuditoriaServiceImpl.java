package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.entity.AuditoriaEntity;
import com.chancla.chancla_lite_auth.repository.AuditoriaRepository;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;

    @Override
    @Transactional
    public void registrar(String usuario, String accion, String entidad, Long entidadId, String detalles) {
        AuditoriaEntity log = new AuditoriaEntity();
        log.setUsuario(usuario);
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setEntidadId(entidadId);
        log.setDetalles(detalles);
        auditoriaRepository.save(log);
    }
}
