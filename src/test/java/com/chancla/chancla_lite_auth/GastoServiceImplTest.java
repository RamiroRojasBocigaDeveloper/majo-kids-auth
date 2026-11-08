package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.GastoRequest;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.GastoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaGastoRepository;
import com.chancla.chancla_lite_auth.repository.GastoRepository;
import com.chancla.chancla_lite_auth.repository.SesionTrabajoRepository;
import com.chancla.chancla_lite_auth.repository.SubcategoriaGastoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import com.chancla.chancla_lite_auth.service.impl.GastoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GastoServiceImplTest {

    @Mock
    private GastoRepository gastoRepository;
    @Mock
    private SesionTrabajoRepository sesionTrabajoRepository;
    @Mock
    private CategoriaGastoRepository categoriaGastoRepository;
    @Mock
    private SubcategoriaGastoRepository subcategoriaGastoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuditoriaService auditoriaService;
    @Mock
    private GastoMapper gastoMapper;

    @InjectMocks
    private GastoServiceImpl gastoService;

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void autenticar(String rol) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test@local", "pw",
                        List.of(new SimpleGrantedAuthority(rol))));
    }

    private GastoRequest requestGastoGeneral(Long sesionId) {
        GastoRequest request = new GastoRequest();
        request.setSesionId(sesionId);
        request.setCategoriaGastoId(1);
        request.setDescripcion("Gasto general de prueba");
        request.setMonto(15000.0);
        request.setEsDeCaja(false);
        return request;
    }

    private SesionTrabajoEntity sesion(EstadoSesion estado) {
        SesionTrabajoEntity sesion = new SesionTrabajoEntity();
        sesion.setId(1L);
        sesion.setEstado(estado);
        return sesion;
    }

    private CategoriaGastoEntity categoria() {
        CategoriaGastoEntity categoria = new CategoriaGastoEntity();
        categoria.setId(1);
        categoria.setNombre("Operativo");
        return categoria;
    }

    private void stubCreacionExitosa() {
        when(categoriaGastoRepository.findById(1)).thenReturn(Optional.of(categoria()));
        when(gastoMapper.toEntity(any(GastoRequest.class))).thenAnswer(invocation -> {
            GastoRequest request = invocation.getArgument(0);
            GastoEntity entidad = new GastoEntity();
            entidad.setDescripcion(request.getDescripcion());
            entidad.setMonto(request.getMonto());
            return entidad;
        });
        when(gastoRepository.save(any(GastoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(gastoMapper.toResponse(any(GastoEntity.class))).thenReturn(new GastoResponse());
    }

    @Test
    void vendedorPuedeRegistrarGastoGeneralConSesionAbierta() {
        autenticar("ROLE_VENDEDOR");
        stubCreacionExitosa();
        SesionTrabajoEntity sesion = sesion(EstadoSesion.ABIERTA);
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion));

        GastoResponse response = gastoService.crear(requestGastoGeneral(1L));

        ArgumentCaptor<GastoEntity> captor = ArgumentCaptor.forClass(GastoEntity.class);
        verify(gastoRepository).save(captor.capture());
        GastoEntity guardado = captor.getValue();

        assertFalse(guardado.isEsDeCaja());
        assertEquals(sesion, guardado.getSesion());
        assertEquals(categoria(), guardado.getCategoriaGasto());
        assertEquals("Gasto general de prueba", guardado.getDescripcion());
        assertEquals(15000.0, guardado.getMonto());
    }

    @Test
    void jefePuedeRegistrarGastoGeneralConSesionAbierta() {
        autenticar("ROLE_JEFE");
        stubCreacionExitosa();
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion(EstadoSesion.ABIERTA)));

        gastoService.crear(requestGastoGeneral(1L));

        verify(gastoRepository).save(any(GastoEntity.class));
    }

    @Test
    void vendedorGastoGeneralSinSesionEsRechazado() {
        autenticar("ROLE_VENDEDOR");

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> gastoService.crear(requestGastoGeneral(null)));

        assertTrue(error.getMessage().contains("sesión abierta"));
        verify(gastoRepository, never()).save(any(GastoEntity.class));
    }

    @Test
    void vendedorGastoGeneralEnSesionCerradaEsRechazado() {
        autenticar("ROLE_VENDEDOR");
        when(sesionTrabajoRepository.findById(1L))
                .thenReturn(Optional.of(sesion(EstadoSesion.CERRADA)));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> gastoService.crear(requestGastoGeneral(1L)));

        assertTrue(error.getMessage().contains("cerrada"));
        verify(gastoRepository, never()).save(any(GastoEntity.class));
    }
}
