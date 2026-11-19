package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.CompraMercanciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompraMercanciaRepository extends JpaRepository<CompraMercanciaEntity, Long> {

    List<CompraMercanciaEntity> findAllByOrderByFechaDescCreatedAtDesc();

    List<CompraMercanciaEntity> findByFechaBetweenOrderByFechaDescCreatedAtDesc(LocalDate inicio, LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c " +
            "WHERE c.fecha BETWEEN :inicio AND :fin")
    Double sumComprasEntre(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c " +
            "WHERE c.fecha <= :fin")
    Double sumComprasHasta(@Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c")
    Double sumCompras();

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c " +
            "WHERE c.detallada = false AND c.fecha BETWEEN :inicio AND :fin")
    Double sumNoDetalladasEntre(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c " +
            "WHERE c.detallada = false AND c.fecha <= :fin")
    Double sumNoDetalladasHasta(@Param("fin") LocalDate fin);

    @Query("SELECT COALESCE(SUM(c.montoTotal), 0.0) FROM CompraMercanciaEntity c " +
            "WHERE c.detallada = false")
    Double sumNoDetalladas();
}
