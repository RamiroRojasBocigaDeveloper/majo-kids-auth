package com.chancla.chancla_lite_auth.repository;

import com.chancla.chancla_lite_auth.dto.response.InversionProductoDTO;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventarioEntity, Long> {
    List<MovimientoInventarioEntity> findByProductoId(Long productoId);
    boolean existsByProductoId(Long productoId);

    @Query("SELECT COALESCE(SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad), 0.0) " +
            "FROM MovimientoInventarioEntity m " +
            "WHERE m.tipo = :tipo AND m.esCompra = true AND m.createdAt BETWEEN :inicio AND :fin")
    Double sumComprasEntre(@Param("tipo") TipoMovimiento tipo,
                           @Param("inicio") LocalDateTime inicio,
                           @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad), 0.0) " +
            "FROM MovimientoInventarioEntity m " +
            "WHERE m.tipo = :tipo AND m.esCompra = true AND m.createdAt <= :fin")
    Double sumComprasHasta(@Param("tipo") TipoMovimiento tipo,
                           @Param("fin") LocalDateTime fin);

    @Query("SELECT COALESCE(SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad), 0.0) " +
            "FROM MovimientoInventarioEntity m " +
            "WHERE m.tipo = :tipo AND m.esCompra = true")
    Double sumCompras(@Param("tipo") TipoMovimiento tipo);

    @Query("SELECT new com.chancla.chancla_lite_auth.dto.response.InversionProductoDTO(" +
            "m.producto.id, m.producto.referencia, m.producto.nombre, " +
            "SUM(m.cantidad), SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad)) " +
            "FROM MovimientoInventarioEntity m " +
            "WHERE m.tipo = :tipo AND m.esCompra = true AND m.createdAt BETWEEN :inicio AND :fin " +
            "GROUP BY m.producto.id, m.producto.referencia, m.producto.nombre " +
            "ORDER BY SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad) DESC")
    List<InversionProductoDTO> findComprasPorProductoEntre(@Param("tipo") TipoMovimiento tipo,
                                                           @Param("inicio") LocalDateTime inicio,
                                                           @Param("fin") LocalDateTime fin);

    @Query("SELECT new com.chancla.chancla_lite_auth.dto.response.InversionProductoDTO(" +
            "m.producto.id, m.producto.referencia, m.producto.nombre, " +
            "SUM(m.cantidad), SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad)) " +
            "FROM MovimientoInventarioEntity m " +
            "WHERE m.tipo = :tipo AND m.esCompra = true " +
            "GROUP BY m.producto.id, m.producto.referencia, m.producto.nombre " +
            "ORDER BY SUM(COALESCE(m.costoUnitario, 0.0) * m.cantidad) DESC")
    List<InversionProductoDTO> findComprasPorProducto(@Param("tipo") TipoMovimiento tipo);
}
