package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.CategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaGastoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;
import com.chancla.chancla_lite_auth.mapper.CategoriaGastoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaGastoRepository;
import com.chancla.chancla_lite_auth.service.CategoriaGastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriaGastoServiceImpl implements CategoriaGastoService {

    private final CategoriaGastoRepository categoriaGastoRepository;
    private final CategoriaGastoMapper categoriaGastoMapper;

    @Autowired
    public CategoriaGastoServiceImpl(CategoriaGastoRepository categoriaGastoRepository,
                                     CategoriaGastoMapper categoriaGastoMapper) {
        this.categoriaGastoRepository = categoriaGastoRepository;
        this.categoriaGastoMapper = categoriaGastoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaGastoResponse> obtenerTodas() {
        List<CategoriaGastoEntity> categorias = categoriaGastoRepository.findAll();
        return categoriaGastoMapper.toResponseList(categorias);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaGastoResponse obtenerPorId(Integer id) {
        CategoriaGastoEntity categoria = categoriaGastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));
        return categoriaGastoMapper.toResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaGastoResponse obtenerPorNombre(String nombre) {
        CategoriaGastoEntity categoria = categoriaGastoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada: " + nombre));
        return categoriaGastoMapper.toResponse(categoria);
    }

    @Override
    public CategoriaGastoResponse crear(CategoriaGastoRequest request) {
        // Validar que el nombre no exista
        if (categoriaGastoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        CategoriaGastoEntity nuevaCategoria = categoriaGastoMapper.toEntity(request);
        CategoriaGastoEntity categoriaGuardada = categoriaGastoRepository.save(nuevaCategoria);
        return categoriaGastoMapper.toResponse(categoriaGuardada);
    }

    @Override
    public CategoriaGastoResponse actualizar(Integer id, CategoriaGastoRequest request) {
        CategoriaGastoEntity categoriaExistente = categoriaGastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id));

        // Validar que el nuevo nombre no exista en otra categoría
        if (!categoriaExistente.getNombre().equalsIgnoreCase(request.getNombre()) &&
                categoriaGastoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe otra categoría con el nombre: " + request.getNombre());
        }

        categoriaGastoMapper.updateEntityFromRequest(request, categoriaExistente);
        CategoriaGastoEntity categoriaActualizada = categoriaGastoRepository.save(categoriaExistente);
        return categoriaGastoMapper.toResponse(categoriaActualizada);
    }

    @Override
    public void eliminar(Integer id) {
        if (!categoriaGastoRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada con ID: " + id);
        }
        categoriaGastoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return categoriaGastoRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaGastoResponse> buscarPorNombre(String nombre) {
        List<CategoriaGastoEntity> categorias = categoriaGastoRepository.findByNombreContainingIgnoreCase(nombre);
        return categoriaGastoMapper.toResponseList(categorias);
    }
}