package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.mapper.ProductoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.DetalleVentaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import com.chancla.chancla_lite_auth.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final MovimientoInventarioRepository movimientoInventarioRepository;
    private final AuditoriaService auditoriaService;
    private final ProductoMapper productoMapper;

    @Autowired
    public ProductoServiceImpl(ProductoRepository productoRepository,
                               CategoriaRepository categoriaRepository,
                               DetalleVentaRepository detalleVentaRepository,
                               MovimientoInventarioRepository movimientoInventarioRepository,
                               AuditoriaService auditoriaService,
                               ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.movimientoInventarioRepository = movimientoInventarioRepository;
        this.auditoriaService = auditoriaService;
        this.productoMapper = productoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerTodos() {
        return productoMapper.toResponseList(productoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return productoMapper.toResponse(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorReferencia(String referencia) {
        ProductoEntity producto = productoRepository.findByReferenciaIgnoreCase(referencia)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con referencia: " + referencia));
        return productoMapper.toResponse(producto);
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        if (productoRepository.existsByReferenciaIgnoreCase(request.getReferencia())) {
            throw new RuntimeException("Ya existe un producto con la referencia: " + request.getReferencia());
        }

        CategoriaEntity categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));

        ProductoEntity nuevoProducto = productoMapper.toEntity(request);
        nuevoProducto.setCategoria(categoria);
        
        ProductoEntity productoGuardado = productoRepository.save(nuevoProducto);
        
        // Registro de Auditoría
        String usuarioActual = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaService.registrar(usuarioActual, "CREAR", "PRODUCTO", productoGuardado.getId(), 
            "Producto creado: " + productoGuardado.getNombre() + " (Ref: " + productoGuardado.getReferencia() + ")");

        return productoMapper.toResponse(productoGuardado);
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        ProductoEntity productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));

        // Validar referencia única si cambia
        if (!productoExistente.getReferencia().equalsIgnoreCase(request.getReferencia()) &&
                productoRepository.existsByReferenciaIgnoreCase(request.getReferencia())) {
            throw new RuntimeException("Ya existe otra opción con la referencia: " + request.getReferencia());
        }

        // Validar categoría si cambia
        if (!productoExistente.getCategoria().getId().equals(request.getCategoriaId())) {
            CategoriaEntity nuevaCategoria = categoriaRepository.findById(request.getCategoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + request.getCategoriaId()));
            productoExistente.setCategoria(nuevaCategoria);
        }

        productoMapper.updateEntityFromRequest(request, productoExistente);
        ProductoEntity productoActualizado = productoRepository.save(productoExistente);

        // Registro de Auditoría
        String usuarioActual = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaService.registrar(usuarioActual, "ACTUALIZAR", "PRODUCTO", productoActualizado.getId(), 
            "Producto actualizado: " + productoActualizado.getNombre() + ". Stock: " + productoActualizado.getStockActual());

        return productoMapper.toResponse(productoActualizado);
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }

        // Protección: No borrar si hay ventas históricas
        if (detalleVentaRepository.existsByProductoId(id)) {
            throw new RuntimeException("No se puede eliminar el producto porque tiene ventas asociadas. Considere inactivarlo en su lugar.");
        }

        // Protección: No borrar si hay registros de inventario
        if (movimientoInventarioRepository.existsByProductoId(id)) {
            throw new RuntimeException("No se puede eliminar el producto porque tiene movimientos de inventario registrados.");
        }

        productoRepository.deleteById(id);

        // Registro de Auditoría
        String usuarioActual = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        auditoriaService.registrar(usuarioActual, "ELIMINAR", "PRODUCTO", id, "Producto eliminado definitivamente.");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        String term = "%" + nombre.toLowerCase() + "%";
        return productoMapper.toResponseList(productoRepository.buscarDinamico(term));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorCategoria(Integer categoriaId) {
        return productoMapper.toResponseList(productoRepository.findByCategoriaId(categoriaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> obtenerStockBajo() {
        // Obtenemos todos los productos activos y delegamos la lógica de stock bajo
        // Implementación segura contra valores nulos
        List<ProductoEntity> productosBase = productoRepository.findAll();
        List<ProductoEntity> bajoStock = productosBase.stream()
                .filter(p -> Boolean.TRUE.equals(p.getActivo())) // Protección contra nulos
                .filter(p -> p.getStockActual() != null && p.getStockMinimo() != null)
                .filter(p -> p.getStockActual() <= p.getStockMinimo())
                .toList();
        return productoMapper.toResponseList(bajoStock);
    }
}
