package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SesionTrabajoRepository extends JpaRepository<SesionTrabajoEntity, Long> {
    List<SesionTrabajoEntity> findByUsuarioId(Long usuarioId);
    Optional<SesionTrabajoEntity> findByUsuarioIdAndEstado(Long usuarioId, EstadoSesion estado);
    List<SesionTrabajoEntity> findByEstado(EstadoSesion estado);
}
