package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SueldoPagadoRepository extends JpaRepository<SueldoPagadoEntity, Long> {
    List<SueldoPagadoEntity> findByUsuarioId(Long usuarioId);
    List<SueldoPagadoEntity> findBySesionId(Long sesionId);
    List<SueldoPagadoEntity> findByFechaPagoBetween(LocalDate fechaInicio, LocalDate fechaFin);
    boolean existsByUsuarioIdAndFechaPago(Long usuarioId, LocalDate fechaPago);
}
