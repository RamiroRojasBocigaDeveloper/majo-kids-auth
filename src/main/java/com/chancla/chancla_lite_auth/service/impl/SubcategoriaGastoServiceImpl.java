package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.SubcategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.SubcategoriaGastoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;
import com.chancla.chancla_lite_auth.entity.SubcategoriaGastoEntity;
import com.chancla.chancla_lite_auth.mapper.SubcategoriaGastoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaGastoRepository;
import com.chancla.chancla_lite_auth.repository.SubcategoriaGastoRepository;
import com.chancla.chancla_lite_auth.service.SubcategoriaGastoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SubcategoriaGastoServiceImpl implements SubcategoriaGastoService {

    private final SubcategoriaGastoRepository subcategoriaGastoRepository;
    private final CategoriaGastoRepository categoriaGastoRepository;
    private final SubcategoriaGastoMapper subcategoriaGastoMapper;

    @Autowired
    public SubcategoriaGastoServiceImpl(SubcategoriaGastoRepository subcategoriaGastoRepository,
                                        CategoriaGastoRepository categoriaGastoRepository,
                                        SubcategoriaGastoMapper subcategoriaGastoMapper) {
        this.subcategoriaGastoRepository = subcategoriaGastoRepository;
        this.categoriaGastoRepository = categoriaGastoRepository;
        this.subcategoriaGastoMapper = subcategoriaGastoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoriaGastoResponse> obtenerTodas() {
        return subcategoriaGastoMapper.toResponseList(subcategoriaGastoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoriaGastoResponse> obtenerPorCategoria(Integer categoriaGastoId) {
        return subcategoriaGastoMapper.toResponseList(subcategoriaGastoRepository.findByCategoriaGastoId(categoriaGastoId));
    }

    @Override
    public SubcategoriaGastoResponse crear(SubcategoriaGastoRequest request) {
        if (subcategoriaGastoRepository.existsByNombreIgnoreCaseAndCategoriaGastoId(request.getNombre(), request.getCategoriaGastoId())) {
            throw new RuntimeException("Ya existe una subcategoría con ese nombre en esta categoría.");
        }

        CategoriaGastoEntity categoria = categoriaGastoRepository.findById(request.getCategoriaGastoId())
                .orElseThrow(() -> new RuntimeException("Categoría de gasto no encontrada."));

        SubcategoriaGastoEntity nuevaSubcategoria = subcategoriaGastoMapper.toEntity(request);
        nuevaSubcategoria.setCategoriaGasto(categoria);

        return subcategoriaGastoMapper.toResponse(subcategoriaGastoRepository.save(nuevaSubcategoria));
    }

    @Override
    public SubcategoriaGastoResponse actualizar(Long id, SubcategoriaGastoRequest request) {
        SubcategoriaGastoEntity existente = subcategoriaGastoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada."));

        if (!existente.getNombre().equalsIgnoreCase(request.getNombre()) &&
                subcategoriaGastoRepository.existsByNombreIgnoreCaseAndCategoriaGastoId(request.getNombre(), request.getCategoriaGastoId())) {
            throw new RuntimeException("Ya existe otra subcategoría con ese nombre en esta categoría.");
        }

        if (!existente.getCategoriaGasto().getId().equals(request.getCategoriaGastoId())) {
            CategoriaGastoEntity nuevaCategoria = categoriaGastoRepository.findById(request.getCategoriaGastoId())
                    .orElseThrow(() -> new RuntimeException("Nueva categoría no encontrada."));
            existente.setCategoriaGasto(nuevaCategoria);
        }

        subcategoriaGastoMapper.updateEntityFromRequest(request, existente);
        return subcategoriaGastoMapper.toResponse(subcategoriaGastoRepository.save(existente));
    }

    @Override
    public void eliminar(Long id) {
        if (!subcategoriaGastoRepository.existsById(id)) {
            throw new RuntimeException("Subcategoría no encontrada.");
        }
        subcategoriaGastoRepository.deleteById(id);
    }
}
