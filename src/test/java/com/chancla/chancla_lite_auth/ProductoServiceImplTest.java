package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.entity.CategoriaEntity;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.ProductoMapper;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.DetalleVentaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.AuditoriaService;
import com.chancla.chancla_lite_auth.service.impl.ProductoServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private DetalleVentaRepository detalleVentaRepository;
    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;
    @Mock
    private AuditoriaService auditoriaService;
    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoServiceImpl productoService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("admin@test.com", "pw",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private ProductoRequest request() {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Moño Rosa");
        request.setReferencia("SAN-001");
        request.setCategoriaId(1);
        request.setPrecioCompra(50.0);
        request.setPrecioVenta(120.0);
        request.setStockActual(10);
        request.setStockMinimo(2);
        return request;
    }

    private ProductoEntity producto(int stock, Double precioCompra) {
        ProductoEntity producto = new ProductoEntity();
        producto.setNombre("Moño Rosa");
        producto.setReferencia("SAN-001");
        producto.setStockActual(stock);
        producto.setPrecioCompra(precioCompra);
        return producto;
    }

    private CategoriaEntity categoria() {
        CategoriaEntity categoria = new CategoriaEntity();
        categoria.setId(1);
        categoria.setNombre("Accesorios");
        return categoria;
    }

    private void stubCreacion(ProductoEntity entidadMapeada) {
        when(productoRepository.existsByReferenciaIgnoreCase("SAN-001")).thenReturn(false);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria()));
        when(productoMapper.toEntity(any(ProductoRequest.class))).thenReturn(entidadMapeada);
        when(productoRepository.save(any(ProductoEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(productoMapper.toResponse(any(ProductoEntity.class)))
                .thenReturn(new com.chancla.chancla_lite_auth.dto.response.ProductoResponse());
    }

    @Test
    void crearConStockInicialRegistraMovimientoDeCompra() {
        ProductoEntity producto = producto(10, 50.0);
        stubCreacion(producto);

        productoService.crear(request());

        ArgumentCaptor<MovimientoInventarioEntity> captor =
                ArgumentCaptor.forClass(MovimientoInventarioEntity.class);
        verify(movimientoInventarioRepository).save(captor.capture());
        MovimientoInventarioEntity movimiento = captor.getValue();

        assertEquals(producto, movimiento.getProducto());
        assertEquals(TipoMovimiento.ENTRADA, movimiento.getTipo());
        assertEquals(10, movimiento.getCantidad());
        assertEquals(50.0, movimiento.getCostoUnitario());
        assertEquals(Boolean.TRUE, movimiento.getEsCompra());
        assertEquals("Alta de producto - stock inicial", movimiento.getMotivo());
        assertEquals(10, producto.getStockActual());
    }

    @Test
    void crearSinStockInicialNoRegistraMovimiento() {
        ProductoEntity producto = producto(0, 50.0);
        stubCreacion(producto);

        productoService.crear(request());

        verify(movimientoInventarioRepository, never()).save(any(MovimientoInventarioEntity.class));
        assertEquals(0, producto.getStockActual());
    }
}
