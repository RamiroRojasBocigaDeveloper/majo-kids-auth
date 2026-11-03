package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.mapper.MovimientoInventarioMapper;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.impl.MovimientoInventarioServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoInventarioServiceImplTest {

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private MovimientoInventarioMapper movimientoInventarioMapper;

    @InjectMocks
    private MovimientoInventarioServiceImpl movimientoService;

    private ProductoEntity producto() {
        ProductoEntity producto = new ProductoEntity();
        producto.setId(1L);
        producto.setNombre("Moño");
        producto.setStockActual(5);
        producto.setPrecioCompra(100.0);
        producto.setPrecioVenta(200.0);
        return producto;
    }

    private MovimientoInventarioRequest request(String tipo, int cantidad, Double costo) {
        MovimientoInventarioRequest request = new MovimientoInventarioRequest();
        request.setProductoId(1L);
        request.setTipo(tipo);
        request.setCantidad(cantidad);
        request.setMotivo("Prueba");
        request.setNuevoPrecioCompra(costo);
        return request;
    }

    private void stubMovimiento(ProductoEntity producto) {
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));
        when(movimientoInventarioMapper.toEntity(any())).thenReturn(new MovimientoInventarioEntity());
        when(movimientoInventarioRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(movimientoInventarioMapper.toResponse(any())).thenReturn(new MovimientoInventarioResponse());
    }

    @Test
    void entradaSinCostoLanzaExcepcion() {
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto()));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> movimientoService.registrarMovimiento(request("ENTRADA", 3, null)));

        assertTrue(error.getMessage().toLowerCase().contains("costo"));
        verify(movimientoInventarioRepository, never()).save(any());
    }

    @Test
    void entradaConCostoMarcaCompraYPersisteCosto() {
        ProductoEntity producto = producto();
        stubMovimiento(producto);

        movimientoService.registrarMovimiento(request("ENTRADA", 3, 250.0));

        ArgumentCaptor<MovimientoInventarioEntity> captor = ArgumentCaptor.forClass(MovimientoInventarioEntity.class);
        verify(movimientoInventarioRepository).save(captor.capture());
        MovimientoInventarioEntity guardado = captor.getValue();

        assertEquals(250.0, guardado.getCostoUnitario());
        assertEquals(Boolean.TRUE, guardado.getEsCompra());
        assertEquals(8, producto.getStockActual());
        assertEquals(250.0, producto.getPrecioCompra());
    }

    @Test
    void salidaNoMarcaCompra() {
        ProductoEntity producto = producto();
        stubMovimiento(producto);

        movimientoService.registrarMovimiento(request("SALIDA", 2, null));

        ArgumentCaptor<MovimientoInventarioEntity> captor = ArgumentCaptor.forClass(MovimientoInventarioEntity.class);
        verify(movimientoInventarioRepository).save(captor.capture());
        MovimientoInventarioEntity guardado = captor.getValue();

        assertNull(guardado.getCostoUnitario());
        assertEquals(Boolean.FALSE, guardado.getEsCompra());
        assertEquals(3, producto.getStockActual());
    }
}
