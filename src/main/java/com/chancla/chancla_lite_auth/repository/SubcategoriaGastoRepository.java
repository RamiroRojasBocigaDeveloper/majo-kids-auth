package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.SubcategoriaGastoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoriaGastoRepository extends JpaRepository<SubcategoriaGastoEntity, Long> {
    List<SubcategoriaGastoEntity> findByCategoriaGastoId(Integer categoriaGastoId);
    List<SubcategoriaGastoEntity> findByNombreContainingIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndCategoriaGastoId(String nombre, Integer categoriaGastoId);
}
