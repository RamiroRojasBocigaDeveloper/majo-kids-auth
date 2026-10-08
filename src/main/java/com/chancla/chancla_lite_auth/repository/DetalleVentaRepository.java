package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentaEntity, Long> {
    List<DetalleVentaEntity> findByVentaId(Long ventaId);
    boolean existsByProductoId(Long productoId);

    @Query("SELECT COALESCE(SUM((d.precioUnitario - d.costoUnitario) * d.cantidad), 0.0) FROM DetalleVentaEntity d WHERE d.venta IN :ventas")
    Double sumMargenByVentas(@Param("ventas") List<VentaEntity> ventas);

    @Query("SELECT COALESCE(SUM(d.cantidad * d.costoUnitario), 0.0) FROM DetalleVentaEntity d WHERE d.venta IN :ventas")
    Double sumCostoByVentas(@Param("ventas") List<VentaEntity> ventas);

    void deleteByVentaId(Long ventaId);
}
