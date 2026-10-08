package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CategoriaGastoRepository extends JpaRepository<CategoriaGastoEntity, Integer> {
    Optional<CategoriaGastoEntity> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCase(String nombre);
    List<CategoriaGastoEntity> findByNombreContainingIgnoreCase(String nombre);


}
