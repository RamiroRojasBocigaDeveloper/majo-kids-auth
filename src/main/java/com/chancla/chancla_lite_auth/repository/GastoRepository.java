package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.GastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GastoRepository extends JpaRepository<GastoEntity, Long> {
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"categoriaGasto", "subcategoriaGasto", "sesion", "sesion.usuario"})
    List<GastoEntity> findAll();

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"categoriaGasto", "subcategoriaGasto", "sesion", "sesion.usuario"})
    List<GastoEntity> findBySesionId(Long sesionId);

    List<GastoEntity> findByCategoriaGastoId(Integer categoriaGastoId);
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"categoriaGasto", "subcategoriaGasto", "sesion", "sesion.usuario"})
    @Query("SELECT g FROM GastoEntity g WHERE COALESCE(g.fechaRegistroManual, g.createdAt) BETWEEN :start AND :end")
    List<GastoEntity> findByRangoFechas(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
