package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.SaldoInicialMercanciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SaldoInicialMercanciaRepository extends JpaRepository<SaldoInicialMercanciaEntity, Long> {

    @Query("SELECT COALESCE(SUM(s.montoBase), 0.0) FROM SaldoInicialMercanciaEntity s")
    Double sumMontoBase();

    @Query("SELECT MIN(s.fechaCorte) FROM SaldoInicialMercanciaEntity s")
    LocalDate findFechaCorte();
}
