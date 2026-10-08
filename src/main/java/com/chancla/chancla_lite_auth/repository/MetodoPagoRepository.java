package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.MetodoPagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetodoPagoRepository extends JpaRepository<MetodoPagoEntity, Integer> {
    Optional<MetodoPagoEntity> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<MetodoPagoEntity> findByNombreContainingIgnoreCase(String nombre) ;
}
