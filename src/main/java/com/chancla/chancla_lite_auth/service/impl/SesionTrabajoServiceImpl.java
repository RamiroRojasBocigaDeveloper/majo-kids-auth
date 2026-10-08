package com.chancla.chancla_lite_auth.service.impl;

import com.chancla.chancla_lite_auth.dto.response.SesionTrabajoResponse;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.SesionTrabajoMapper;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.ProductoService;
import com.chancla.chancla_lite_auth.service.SesionTrabajoService;
import com.chancla.chancla_lite_auth.dto.response.ResumenSesionResponse;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.VentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class SesionTrabajoServiceImpl implements SesionTrabajoService {

    private static final Logger log = LoggerFactory.getLogger(SesionTrabajoServiceImpl.class);

    private final SesionTrabajoRepository sesionTrabajoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoService productoService;
    private final SesionTrabajoMapper sesionTrabajoMapper;
    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;

    @Autowired
    public SesionTrabajoServiceImpl(SesionTrabajoRepository sesionTrabajoRepository,
                                    UsuarioRepository usuarioRepository,
                                    ProductoService productoService,
                                    SesionTrabajoMapper sesionTrabajoMapper,
                                    VentaRepository ventaRepository,
                                    GastoRepository gastoRepository) {
        this.sesionTrabajoRepository = sesionTrabajoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoService = productoService;
        this.sesionTrabajoMapper = sesionTrabajoMapper;
        this.ventaRepository = ventaRepository;
        this.gastoRepository = gastoRepository;
    }

    @Override
    public SesionTrabajoResponse abrirSesion(Long usuarioId) {
        log.info("Abriendo sesión para usuario ID: {}", usuarioId);
        // Verificar si ya tiene una sesión abierta
        if (sesionTrabajoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoSesion.ABIERTA).isPresent()) {
            log.warn("El usuario ID {} ya tiene una sesión abierta", usuarioId);
            throw new RuntimeException("El usuario ya tiene una sesión abierta.");
        }

        UsuarioEntity usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        SesionTrabajoEntity nuevaSesion = new SesionTrabajoEntity();
        nuevaSesion.setUsuario(usuario);
        nuevaSesion.setHoraInicio(LocalDateTime.now());
        nuevaSesion.setEstado(EstadoSesion.ABIERTA);
        
        // Obtener el rol del usuario (ManyToOne)
        if (usuario.getRol() != null) {
            nuevaSesion.setRolUsuario(usuario.getRol().getNombre());
        }

        SesionTrabajoEntity guardada = sesionTrabajoRepository.save(nuevaSesion);
        return sesionTrabajoMapper.toResponse(guardada);
    }

    @Override
    public SesionTrabajoResponse cerrarSesion(Long sesionId) {
        log.info("Petición para cerrar sesión ID: {}", sesionId);
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        if (sesion.getEstado() != EstadoSesion.ABIERTA) {
            log.warn("Intento de cerrar sesión ID {} que no está abierta. Estado actual: {}. (Usuario ID: {})", 
                    sesionId, sesion.getEstado(), sesion.getUsuario().getId());
            throw new RuntimeException("La sesión ya está cerrada o en un estado inválido (Estado actual: " + sesion.getEstado() + ").");
        }

        sesion.setHoraFin(LocalDateTime.now());
        sesion.setEstado(EstadoSesion.CERRADA);

        SesionTrabajoEntity actualizada = sesionTrabajoRepository.save(sesion);
        log.info("Sesion ID {} cerrada correctamente. Calculando alertas de stock...", sesionId);
        
        SesionTrabajoResponse response = sesionTrabajoMapper.toResponse(actualizada);
        
        // Adjuntar alertas de stock bajo (operación no crítica, protegida contra fallos)
        try {
            response.setAlertasStock(productoService.obtenerStockBajo());
            log.info("Cierre de sesion ID {} completado con {} alertas de stock", sesionId, response.getAlertasStock().size());
        } catch (Exception e) {
            log.error("Fallo al calcular alertas de stock durante el cierre: {}. Continuando cierre...", e.getMessage());
            // No bloqueamos el retorno de la sesión cerrada por un fallo en las alertas
            response.setAlertasStock(java.util.Collections.emptyList());
        }
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public SesionTrabajoResponse obtenerSesionActiva(Long usuarioId) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoSesion.ABIERTA)
                .orElseThrow(() -> new RuntimeException("No se encontró sesión activa para el usuario."));
        return sesionTrabajoMapper.toResponse(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionTrabajoResponse> obtenerHistorialUsuario(Long usuarioId) {
        return sesionTrabajoMapper.toResponseList(sesionTrabajoRepository.findByUsuarioId(usuarioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SesionTrabajoResponse> obtenerTodas() {
        return sesionTrabajoMapper.toResponseList(sesionTrabajoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public SesionTrabajoResponse obtenerPorId(Long id) {
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));
        return sesionTrabajoMapper.toResponse(sesion);
    }

    @Override
    @Transactional(readOnly = true)
    public ResumenSesionResponse obtenerResumenCierre(Long sesionId) {
        log.info("Calculando resumen para la sesión ID: {}", sesionId);

        // Validar que exista la sesión
        SesionTrabajoEntity sesion = sesionTrabajoRepository.findById(sesionId)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada."));

        List<VentaEntity> ventas = ventaRepository.findBySesionId(sesionId);
        List<GastoEntity> gastos = gastoRepository.findBySesionId(sesionId);

        Double totalVentas = ventas.stream().mapToDouble(VentaEntity::getTotal).sum();
        Double totalGastos = gastos.stream().mapToDouble(GastoEntity::getMonto).sum();
        Double saldoNeto = totalVentas - totalGastos;

        return ResumenSesionResponse.builder()
                .sesionId(sesionId)
                .totalVentas(totalVentas)
                .totalGastos(totalGastos)
                .saldoNeto(saldoNeto)
                .build();
    }
}
