package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.entity.MetodoPagoEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import com.chancla.chancla_lite_auth.enums.EstadoSesion;
import com.chancla.chancla_lite_auth.mapper.VentaMapper;
import com.chancla.chancla_lite_auth.repository.*;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import com.chancla.chancla_lite_auth.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private SesionTrabajoRepository sesionTrabajoRepository;
    @Mock
    private MetodoPagoRepository metodoPagoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuditoriaService auditoriaService;
    @Mock
    private VentaMapper ventaMapper;
    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("vendedor@test.com", "pw",
                        List.of(new SimpleGrantedAuthority("ROLE_VENDEDOR"))));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private VentaRequest detalle(Long productoId, int cantidad, double precio) {
        VentaRequest request = new VentaRequest();
        request.setSesionId(1L);
        request.setMetodoPagoId(1);
        request.setDescuento(0.0);
        VentaRequest.DetalleVentaRequest det = new VentaRequest.DetalleVentaRequest();
        det.setProductoId(productoId);
        det.setCantidad(cantidad);
        det.setPrecioUnitario(precio);
        request.setDetalles(List.of(det));
        return request;
    }

    private void prepararContextoBasico() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre("Vendedor");
        SesionTrabajoEntity sesion = new SesionTrabajoEntity();
        sesion.setId(1L);
        sesion.setUsuario(usuario);
        sesion.setEstado(EstadoSesion.ABIERTA);
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion));

        MetodoPagoEntity metodoPago = new MetodoPagoEntity();
        metodoPago.setId(1);
        metodoPago.setNombre("Efectivo");
        when(metodoPagoRepository.findById(1)).thenReturn(Optional.of(metodoPago));
    }

    @Test
    void rechazaCuandoElStockEsInsuficiente() {
        prepararContextoBasico();

        ProductoEntity producto = new ProductoEntity();
        producto.setId(1L);
        producto.setNombre("Zapatilla Test");
        producto.setPrecioVenta(100.0);
        producto.setPrecioCompra(50.0);
        producto.setStockActual(5);
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ventaService.procesarVenta(detalle(1L, 10, 100.0)));

        assertTrue(ex.getMessage().contains("Stock insuficiente"));
        verify(productoRepository, never()).save(any());
        assertEquals(5, producto.getStockActual());
    }

    @Test
    void descuentaStockYRegistraSalidaCuandoHayDisponible() {
        prepararContextoBasico();

        ProductoEntity producto = new ProductoEntity();
        producto.setId(1L);
        producto.setNombre("Zapatilla Test");
        producto.setPrecioVenta(100.0);
        producto.setPrecioCompra(50.0);
        producto.setStockActual(5);
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));
        when(ventaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ventaService.procesarVenta(detalle(1L, 2, 100.0));

        assertEquals(3, producto.getStockActual());
        verify(productoRepository).save(producto);
        verify(movimientoInventarioRepository).save(any());
        verify(auditoriaService).registrar(any(), any(), any(), any(), any());
    }

    @Test
    void rechazaCuandoLaSesionNoEstaAbierta() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNombre("Vendedor");
        SesionTrabajoEntity sesion = new SesionTrabajoEntity();
        sesion.setId(1L);
        sesion.setUsuario(usuario);
        sesion.setEstado(EstadoSesion.CERRADA);
        when(sesionTrabajoRepository.findById(1L)).thenReturn(Optional.of(sesion));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> ventaService.procesarVenta(detalle(1L, 1, 100.0)));

        assertTrue(ex.getMessage().contains("no está abierta"));
        verify(productoRepository, never()).findByIdForUpdate(any());
    }
}
