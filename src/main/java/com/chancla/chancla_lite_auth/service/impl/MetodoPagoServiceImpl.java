package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.MetodoPagoRequest;
import com.chancla.chancla_lite_auth.dto.response.MetodoPagoResponse;
import com.chancla.chancla_lite_auth.entity.MetodoPagoEntity;
import com.chancla.chancla_lite_auth.mapper.MetodoPagoMapper;
import com.chancla.chancla_lite_auth.repository.MetodoPagoRepository;
import com.chancla.chancla_lite_auth.service.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MetodoPagoServiceImpl implements MetodoPagoService {

    private final MetodoPagoRepository metodoPagoRepository;
    private final MetodoPagoMapper metodoPagoMapper;

    @Autowired
    public MetodoPagoServiceImpl(MetodoPagoRepository metodoPagoRepository,
                                 MetodoPagoMapper metodoPagoMapper) {
        this.metodoPagoRepository = metodoPagoRepository;
        this.metodoPagoMapper = metodoPagoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoResponse> obtenerTodos() {
        List<MetodoPagoEntity> metodos = metodoPagoRepository.findAll();
        return metodoPagoMapper.toResponseList(metodos);
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoResponse obtenerPorId(Integer id) {
        MetodoPagoEntity metodo = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + id));
        return metodoPagoMapper.toResponse(metodo);
    }

    @Override
    @Transactional(readOnly = true)
    public MetodoPagoResponse obtenerPorNombre(String nombre) {
        MetodoPagoEntity metodo = metodoPagoRepository.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado: " + nombre));
        return metodoPagoMapper.toResponse(metodo);
    }

    @Override
    public MetodoPagoResponse crear(MetodoPagoRequest request) {
        if (metodoPagoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe un método de pago con el nombre: " + request.getNombre());
        }

        MetodoPagoEntity nuevoMetodo = metodoPagoMapper.toEntity(request);
        MetodoPagoEntity metodoGuardado = metodoPagoRepository.save(nuevoMetodo);
        return metodoPagoMapper.toResponse(metodoGuardado);
    }

    @Override
    public MetodoPagoResponse actualizar(Integer id, MetodoPagoRequest request) {
        MetodoPagoEntity metodoExistente = metodoPagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Método de pago no encontrado con ID: " + id));

        if (!metodoExistente.getNombre().equalsIgnoreCase(request.getNombre()) &&
                metodoPagoRepository.existsByNombreIgnoreCase(request.getNombre())) {
            throw new RuntimeException("Ya existe otra opción con el nombre: " + request.getNombre());
        }

        metodoPagoMapper.updateEntityFromRequest(request, metodoExistente);
        MetodoPagoEntity metodoActualizado = metodoPagoRepository.save(metodoExistente);
        return metodoPagoMapper.toResponse(metodoActualizado);
    }

    @Override
    public void eliminar(Integer id) {
        if (!metodoPagoRepository.existsById(id)) {
            throw new RuntimeException("Método de pago no encontrado con ID: " + id);
        }
        metodoPagoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombre) {
        return metodoPagoRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MetodoPagoResponse> buscarPorNombre(String nombre) {
        List<MetodoPagoEntity> metodos = metodoPagoRepository.findByNombreContainingIgnoreCase(nombre);
        return metodoPagoMapper.toResponseList(metodos);
    }
}
