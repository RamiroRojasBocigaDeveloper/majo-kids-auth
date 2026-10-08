package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<VentaEntity, Long> {
    
    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detalles", "sesion", "sesion.usuario", "metodoPago"})
    java.util.List<VentaEntity> findAll();

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detalles", "sesion", "sesion.usuario", "metodoPago"})
    List<VentaEntity> findBySesionId(Long sesionId);

    Optional<VentaEntity> findByNumeroFactura(String numeroFactura);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detalles", "sesion", "sesion.usuario", "metodoPago"})
    List<VentaEntity> findBySesionUsuarioId(Long usuarioId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {"detalles", "sesion", "sesion.usuario", "metodoPago"})
    @Query("SELECT v FROM VentaEntity v WHERE COALESCE(v.fechaRegistroManual, v.createdAt) BETWEEN :inicio AND :fin")
    List<VentaEntity> findByRangoFechas(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
}
