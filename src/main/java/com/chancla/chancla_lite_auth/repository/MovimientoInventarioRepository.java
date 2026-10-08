package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventarioEntity, Long> {
    List<MovimientoInventarioEntity> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);
}
