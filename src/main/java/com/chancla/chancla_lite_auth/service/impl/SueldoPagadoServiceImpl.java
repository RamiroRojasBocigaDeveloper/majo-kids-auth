package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.request.SueldoPagadoRequest;
import com.chancla.chancla_lite_auth.dto.response.SueldoPagadoResponse;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.SueldoPagadoMapper;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.SueldoPagadoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.SueldoPagadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SueldoPagadoServiceImpl implements SueldoPagadoService {

    private final SueldoPagadoRepository sueldoPagadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final SueldoPagadoMapper sueldoPagadoMapper;

    @Autowired
    public SueldoPagadoServiceImpl(SueldoPagadoRepository sueldoPagadoRepository,
                                   UsuarioRepository usuarioRepository,
                                   SesionTrabajoRepository sesionTrabajoRepository,
                                   SueldoPagadoMapper sueldoPagadoMapper) {
        this.sueldoPagadoRepository = sueldoPagadoRepository;
        this.usuarioRepository = usuarioRepository;
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.sueldoPagadoMapper = sueldoPagadoMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SueldoPagadoResponse> obtenerTodos() {
        return sueldoPagadoMapper.toResponseList(sueldoPagadoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SueldoPagadoResponse> obtenerPorUsuario(Long usuarioId) {
        return sueldoPagadoMapper.toResponseList(sueldoPagadoRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SueldoPagadoResponse> obtenerPorSesion(Long sesionId) {
        return sueldoPagadoMapper.toResponseList(sueldoPagadoRepository.findBySesionId(sesionId));
    }

    @Override
    public SueldoPagadoResponse registrarPago(SueldoPagadoRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(request.getSesionId())
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        // Verifica si el usuario actual es ADMINISTRADOR
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMINISTRADOR"));

        // Regla: Solo ADMIN puede registrar en sesiones cerradas (Auditoría tardía)
        if (!isAdmin && sesion.getEstado() != EstadoSesion.ABIERTA) {
            throw new RuntimeException("Solo un administrador puede registrar pagos en una sesión cerrada.");
        }

        SueldoPagadoEntity nuevoPago = sueldoPagadoMapper.toEntity(request);
        nuevoPago.setUsuario(usuario);
        nuevoPago.setSesion(sesion);

        return sueldoPagadoMapper.toResponse(sueldoPagadoRepository.save(nuevoPago));
    }

    @Override
    public void eliminarPago(Long id) {
        if (!sueldoPagadoRepository.existsById(id)) {
            throw new RuntimeException("Registro de pago no encontrado.");
        }
        sueldoPagadoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SueldoPagadoResponse obtenerPorId(Long id) {
        SueldoPagadoEntity pago = sueldoPagadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro de pago no encontrado."));
        return sueldoPagadoMapper.toResponse(pago);
    }

    @Override
    public String registrarSueldoManual(Long usuarioId, Long sesionId, Double monto) {
        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        String usuarioRol = usuario.getRol() != null ? usuario.getRol().getNombre() : "";

        if (!usuarioRol.equals("vendedor") && !usuarioRol.equals("administrador")) {
            throw new RuntimeException("Solo vendedores y administradores reciben sueldo");
        }

        boolean yaPagado = sueldoPagadoRepository.existsByUsuarioIdAndFechaPago(usuarioId, java.time.LocalDate.now());

        if (yaPagado) {
            throw new RuntimeException("Ya se registró sueldo para este usuario hoy");
        }

        SueldoPagadoEntity sueldo = new SueldoPagadoEntity();
        sueldo.setUsuario(usuario);
        sueldo.setSesion(sesion);
        sueldo.setMonto(monto);
        sueldo.setFechaPago(java.time.LocalDate.now());
        sueldoPagadoRepository.save(sueldo);

        return "Sueldo registrado correctamente";
    }
}
