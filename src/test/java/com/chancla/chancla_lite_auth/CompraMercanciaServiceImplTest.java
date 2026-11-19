package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.CompraMercanciaRequest;
import com.chancla.chancla_lite_auth.dto.request.DetalleCompraRequest;
import com.chancla.chancla_lite_auth.dto.response.CompraMercanciaResponse;
import com.chancla.chancla_lite_auth.entity.CompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.DetalleCompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.MetodoPagoCompra;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.mapper.CompraMercanciaMapper;
import com.chancla.chancla_lite_auth.repository.CompraMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.DetalleCompraMercanciaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.repository.UsuarioRepository;
import com.chancla.chancla_lite_auth.service.impl.CompraMercanciaServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompraMercanciaServiceImplTest {

    @Mock
    private CompraMercanciaRepository compraMercanciaRepository;
    @Mock
    private DetalleCompraMercanciaRepository detalleCompraMercanciaRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private CompraMercanciaMapper compraMercanciaMapper;

    @InjectMocks
    private CompraMercanciaServiceImpl compraMercanciaService;

    private ProductoEntity producto(Long id, int stock, double precioCompra, double precioVenta) {
        ProductoEntity producto = new ProductoEntity();
        producto.setId(id);
        producto.setNombre("Producto " + id);
        producto.setStockActual(stock);
        producto.setPrecioCompra(precioCompra);
        producto.setPrecioVenta(precioVenta);
        return producto;
    }

    private DetalleCompraRequest linea(Long productoId, int cantidad, double costoUnitario) {
        return linea(productoId, cantidad, costoUnitario, null);
    }

    private DetalleCompraRequest linea(Long productoId, int cantidad, double costoUnitario, Double precioVenta) {
        DetalleCompraRequest linea = new DetalleCompraRequest();
        linea.setProductoId(productoId);
        linea.setCantidad(cantidad);
        linea.setCostoUnitario(costoUnitario);
        linea.setPrecioVenta(precioVenta);
        return linea;
    }

    private CompraMercanciaRequest request(List<DetalleCompraRequest> lineas) {
        CompraMercanciaRequest request = new CompraMercanciaRequest();
        request.setFecha(LocalDate.of(2026, 11, 18));
        request.setProveedor("Proveedor de prueba");
        request.setDescripcion("Compra de prueba");
        request.setMetodoPago(MetodoPagoCompra.EFECTIVO);
        request.setLineas(lineas);
        return request;
    }

    private void stubPersistencia() {
        when(compraMercanciaRepository.save(any())).thenAnswer(invocation -> {
            CompraMercanciaEntity compra = invocation.getArgument(0);
            compra.setId(99L);
            return compra;
        });
        when(detalleCompraMercanciaRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(movimientoInventarioRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(compraMercanciaMapper.toResponse(any(), anyList()))
                .thenReturn(new CompraMercanciaResponse());
    }

    @Test
    void crearCompraActualizaStockCostoPrecioVentaYRegistraEntradaDeInventario() {
        ProductoEntity moño = producto(1L, 5, 100.0, 200.0);
        ProductoEntity cinta = producto(2L, 2, 50.0, 100.0);
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(moño));
        when(productoRepository.findByIdForUpdate(2L)).thenReturn(Optional.of(cinta));
        stubPersistencia();

        compraMercanciaService.crear(request(List.of(
                linea(2L, 3, 80.0, 150.0),
                linea(1L, 4, 250.0, 400.0))));

        assertEquals(9, moño.getStockActual());
        assertEquals(250.0, moño.getPrecioCompra());
        assertEquals(400.0, moño.getPrecioVenta());
        assertEquals(5, cinta.getStockActual());
        assertEquals(80.0, cinta.getPrecioCompra());
        assertEquals(150.0, cinta.getPrecioVenta());

        ArgumentCaptor<CompraMercanciaEntity> compraCaptor =
                ArgumentCaptor.forClass(CompraMercanciaEntity.class);
        verify(compraMercanciaRepository).save(compraCaptor.capture());
        CompraMercanciaEntity compra = compraCaptor.getValue();
        assertEquals(1240.0, compra.getMontoTotal(), 0.001);
        assertEquals(Boolean.TRUE, compra.getDetallada());
        assertEquals(MetodoPagoCompra.EFECTIVO, compra.getMetodoPago());

        ArgumentCaptor<DetalleCompraMercanciaEntity> detalleCaptor =
                ArgumentCaptor.forClass(DetalleCompraMercanciaEntity.class);
        verify(detalleCompraMercanciaRepository, times(2)).save(detalleCaptor.capture());
        assertEquals(1000.0, detalleCaptor.getAllValues().get(0).getSubtotal(), 0.001);
        assertEquals(240.0, detalleCaptor.getAllValues().get(1).getSubtotal(), 0.001);

        ArgumentCaptor<MovimientoInventarioEntity> movimientoCaptor =
                ArgumentCaptor.forClass(MovimientoInventarioEntity.class);
        verify(movimientoInventarioRepository, times(2)).save(movimientoCaptor.capture());
        for (MovimientoInventarioEntity movimiento : movimientoCaptor.getAllValues()) {
            assertEquals(TipoMovimiento.ENTRADA, movimiento.getTipo());
            assertEquals(Boolean.TRUE, movimiento.getEsCompra());
            assertEquals(99L, movimiento.getReferenciaId());
        }
    }

    @Test
    void precioVentaOpcionalMantieneElPrecioActualSiNoSeEnvia() {
        ProductoEntity producto = producto(1L, 5, 100.0, 300.0);
        when(productoRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(producto));
        stubPersistencia();

        compraMercanciaService.crear(request(List.of(linea(1L, 1, 120.0))));

        assertEquals(300.0, producto.getPrecioVenta());
        assertEquals(120.0, producto.getPrecioCompra());
    }

    @Test
    void productoRepetidoLanzaExcepcionYNoPersisteNada() {
        RuntimeException error = assertThrows(RuntimeException.class,
                () -> compraMercanciaService.crear(request(List.of(
                        linea(1L, 2, 100.0),
                        linea(1L, 3, 120.0)))));

        assertTrue(error.getMessage().toLowerCase().contains("repetido"));
        verify(compraMercanciaRepository, never()).save(any());
        verify(movimientoInventarioRepository, never()).save(any());
    }
}
