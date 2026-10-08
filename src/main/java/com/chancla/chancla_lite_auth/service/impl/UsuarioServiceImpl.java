package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.UsuarioRequest;
import com.chancla.chancla_lite_auth.dto.response.UsuarioResponse;
import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.exception.ResourceNotFoundException;
import com.chancla.chancla_lite_auth.mapper.UsuarioMapper;
import com.chancla.chancla_lite_auth.repository.RolRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> obtenerTodos() {
        return usuarioMapper.toResponseList(usuarioRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Long id) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria para nuevos usuarios");
        }

        UsuarioEntity usuario = usuarioMapper.toEntity(request);
        
        RolEntity rol = rolRepository.findById(request.getRolId().shortValue())
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));
        
        usuario.setRol(rol);
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        System.out.println(">>> [DEBUG] Actualizando usuario ID: " + id);
        System.out.println(">>> [DEBUG] Rol ID recibido: " + request.getRolId());

        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));

        System.out.println(">>> [DEBUG] Rol actual en DB: " + (usuario.getRol() != null ? usuario.getRol().getId() : "null"));

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setSueldoDiario(request.getSueldoDiario());
        usuario.setActivo(request.getActivo());

        if (request.getRolId() != null) {
            RolEntity rol = rolRepository.findById(request.getRolId().shortValue())
                    .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con ID: " + request.getRolId()));
            
            System.out.println(">>> [DEBUG] Asignando nuevo rol: " + rol.getNombre() + " (ID: " + rol.getId() + ")");
            usuario.setRol(rol);
            
            // Forzar actualización manual vía query para evitar problemas de Dirty Checking
            usuarioRepository.actualizarRol(id, request.getRolId().shortValue());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Usamos saveAndFlush para obligar a Hibernate a sincronizar con la DB de inmediato
        UsuarioEntity guardado = usuarioRepository.saveAndFlush(usuario);
        
        System.out.println(">>> [DEBUG] Usuario guardado. Nuevo Rol ID en entidad: " + (guardado.getRol() != null ? guardado.getRol().getId() : "null"));
        
        return usuarioMapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstado(Long id, Boolean activo) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setActivo(activo);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void resetearPassword(Long id, String nuevaPassword) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + id));
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
    }
}
