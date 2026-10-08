package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.entity.RolEntity;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.mapper.SueldoPagadoMapper;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.SueldoPagadoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.impl.SueldoPagadoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SueldoPagadoServiceImplTest {

    @Mock
    private SueldoPagadoRepository sueldoPagadoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private SesionTrabajoRepository sesionTrabajoRepository;
    @Mock
    private SueldoPagadoMapper sueldoPagadoMapper;

    @InjectMocks
    private SueldoPagadoServiceImpl sueldoPagadoService;

    private UsuarioEntity usuarioVendedor() {
        RolEntity rol = new RolEntity();
        rol.setNombre("vendedor");
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setNombre("Vendedor Test");
        usuario.setRol(rol);
        return usuario;
    }

    private SesionTrabajoEntity sesion() {
        SesionTrabajoEntity sesion = new SesionTrabajoEntity();
        sesion.setId(1L);
        sesion.setUsuario(usuarioVendedor());
        return sesion;
    }

    @Test
    void registraSueldoSinCrearGastoDeNomina() {
        UsuarioEntity usuario = usuarioVendedor();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion()));
        when(sueldoPagadoRepository.existsByUsuarioIdAndFechaPago(1L, LocalDate.now())).thenReturn(false);

        String resultado = sueldoPagadoService.registrarSueldoManual(1L, 1L, 50000.0);

        assertEquals("Sueldo registrado correctamente", resultado);
        verify(sueldoPagadoRepository).save(any(SueldoPagadoEntity.class));
    }

    @Test
    void rechazaRegistroSiElUsuarioYaFuePagadoHoy() {
        UsuarioEntity usuario = usuarioVendedor();
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion()));
        when(sueldoPagadoRepository.existsByUsuarioIdAndFechaPago(1L, LocalDate.now())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> sueldoPagadoService.registrarSueldoManual(1L, 1L, 50000.0));

        assertTrue(ex.getMessage().contains("Ya se registró sueldo"));
        verify(sueldoPagadoRepository, never()).save(any());
    }

    @Test
    void rechazaUsuariosSinRolDeVendedorOAdministrador() {
        RolEntity rol = new RolEntity();
        rol.setNombre("jefe");
        UsuarioEntity usuario = usuarioVendedor();
        usuario.setRol(rol);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> sueldoPagadoService.registrarSueldoManual(1L, 1L, 50000.0));

        assertTrue(ex.getMessage().contains("Solo vendedores y administradores"));
        verify(sueldoPagadoRepository, never()).save(any());
    }
}
