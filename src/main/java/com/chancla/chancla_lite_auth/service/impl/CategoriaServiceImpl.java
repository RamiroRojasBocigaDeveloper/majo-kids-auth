package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.CategoriaRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaEntity;
import com.chancla.chancla_lite_auth.mapper.CategoriaMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;
    private final ProductoRepository productoRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository,
                                CategoriaMapper categoriaMapper,
                                ProductoRepository productoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
        this.productoRepository = productoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> obtenerTodas() {
        List<CategoriaEntity> categorias = categoriaRepository.findAll();
        return categoriaMapper.toResponseList(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorId(Integer id) {
        CategoriaEntity categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return categoriaMapper.toResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerPorNombre(String nombre) {
        CategoriaEntity categoria = categoriaRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
        return categoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse crear(CategoriaRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }
        validarLongitudMinima(request.getNombre());

        if (categoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        CategoriaEntity nuevaCategoria = categoriaMapper.toEntity(request);
        CategoriaEntity categoriaGuardada = categoriaRepository.save(nuevaCategoria);
        return categoriaMapper.toResponse(categoriaGuardada);
    }

    @Override
    public CategoriaResponse actualizar(Integer id, CategoriaRequest request) {
        if (request.getNombre() != null) {
            request.setNombre(request.getNombre().trim());
        }
        validarLongitudMinima(request.getNombre());

        CategoriaEntity categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        if (!categoriaExistente.getNombre().equalsIgnoreCase(request.getNombre()) &&
                categoriaRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + request.getNombre());
        }

        categoriaMapper.updateEntityFromRequest(request, categoriaExistente);
        CategoriaEntity categoriaActualizada = categoriaRepository.save(categoriaExistente);
        return categoriaMapper.toResponse(categoriaActualizada);
    }

    @Override
    public void eliminar(Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }

        // Protección: No borrar si tiene productos vinculados
        if (productoRepository.existsByCategoriaId(id)) {
            throw new RuntimeException("No se puede eliminar la categoría porque tiene productos asociados. Elimine o mueva los productos primero.");
        }

        categoriaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return categoriaRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> buscarPorNombre(String nombre) {
        List<CategoriaEntity> categorias = categoriaRepository.findByNombreContainingIgnoreCase(nombre);
        return categoriaMapper.toResponseList(categorias);
    }

    private void validarLongitudMinima(String nombre) {
        if (nombre == null || nombre.length() < 3) {
            throw new RuntimeException("El nombre debe tener al menos 3 caracteres");
        }
    }
}
