package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.DetalleCompraMercanciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCompraMercanciaRepository extends JpaRepository<DetalleCompraMercanciaEntity, Long> {
    List<DetalleCompraMercanciaEntity> findByCompraId(Long compraId);
    void deleteByCompraId(Long compraId);
}
