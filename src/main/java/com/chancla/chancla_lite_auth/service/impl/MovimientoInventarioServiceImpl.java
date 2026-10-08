package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.MovimientoInventarioMapper;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.MovimientoInventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final ProductoRepository productoRepository;
    private final MovimientoInventarioMapper movimientoInventarioMapper;

    @Autowired
    public MovimientoInventarioServiceImpl(MovimientoInventarioRepository movimientoInventarioRepository,
                                           ProductoRepository productoRepository,
                                           MovimientoInventarioMapper movimientoInventarioMapper) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.productoRepository = productoRepository;
        this.movimientoInventarioMapper = movimientoInventarioMapper;
    }

    @Override
    public MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request) {
        ProductoEntity producto = productoRepository.findByIdForUpdate(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + request.getProductoId()));

        TipoMovimiento tipo = TipoMovimiento.valueOf(request.getTipo().toUpperCase());

        // Validación previa para asegurar que la acción es posible antes de que el trigger la ejecte
        if (tipo == TipoMovimiento.SALIDA || tipo == TipoMovimiento.AJUSTE) {
        	// Aquí AJUSTE se maneja como correción. Para mantener la misma lógica de antes (salida):
            if (tipo == TipoMovimiento.SALIDA && producto.getStockActual() < request.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para realizar salida manual.");
            }
        }

        // Actualizar precio de producto si se provee
        if (tipo == TipoMovimiento.ENTRADA) {
            boolean actualizoPrecio = false;
            if (request.getNuevoPrecioCompra() != null) {
                producto.setPrecioCompra(request.getNuevoPrecioCompra());
                actualizoPrecio = true;
            }
            if (request.getNuevoPrecioVenta() != null) {
                producto.setPrecioVenta(request.getNuevoPrecioVenta());
                actualizoPrecio = true;
            }
            if (actualizoPrecio) {
                productoRepository.save(producto);
            }
        }

        // Actualizar el stock manualmente (Reemplazo del trigger trg_mov_inv_ai)
        if (tipo == TipoMovimiento.ENTRADA) {
            producto.setStockActual(producto.getStockActual() + request.getCantidad());
        } else if (tipo == TipoMovimiento.SALIDA || tipo == TipoMovimiento.AJUSTE) {
            producto.setStockActual(producto.getStockActual() - request.getCantidad());
        }
        // Guardamos el producto con su nuevo stock y/o precio
        productoRepository.save(producto);

        // Crear Movimiento
        MovimientoInventarioEntity movimento = movimientoInventarioMapper.toEntity(request);
        movimento.setProducto(producto);
        movimento.setTipo(tipo);

        MovimientoInventarioEntity guardado = movimientoInventarioRepository.save(movimento);
        return movimientoInventarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerHistorialPorProducto(Long productoId) {
        return movimientoInventarioMapper.toResponseList(movimientoInventarioRepository.findByProductoId(productoId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoInventarioResponse> obtenerTodoElHistorial() {
        return movimientoInventarioMapper.toResponseList(movimientoInventarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public MovimientoInventarioResponse obtenerPorId(Long id) {
        MovimientoInventarioEntity movimiento = movimientoInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento de inventario no encontrado."));
        return movimientoInventarioMapper.toResponse(movimiento);
    }
}
