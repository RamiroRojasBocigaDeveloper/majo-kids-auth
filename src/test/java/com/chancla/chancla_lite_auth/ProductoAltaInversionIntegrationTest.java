package com.chancla.chancla_lite_auth;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.EstadisticasInversionResponse;
import com.chancla.chancla_lite_auth.dto.response.InversionProductoDTO;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import com.chancla.chancla_lite_auth.entity.AuditoriaEntity;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import com.chancla.chancla_lite_auth.enums.TipoMovimiento;
import com.chancla.chancla_lite_auth.repository.AuditoriaRepository;
import com.chancla.chancla_lite_auth.repository.CategoriaRepository;
import com.chancla.chancla_lite_auth.repository.MovimientoInventarioRepository;
import com.chancla.chancla_lite_auth.repository.ProductoRepository;
import com.chancla.chancla_lite_auth.service.ProductoService;
import com.chancla.chancla_lite_auth.service.ReporteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ProductoAltaInversionIntegrationTest {

    private static final int STOCK_INICIAL = 5;
    private static final double PRECIO_COMPRA = 100.0;
    private static final double MONTO_ESPERADO = STOCK_INICIAL * PRECIO_COMPRA;

    @Autowired
    private ProductoService productoService;
    @Autowired
    private ReporteService reporteService;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MovimientoInventarioRepository movimientoInventarioRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private AuditoriaRepository auditoriaRepository;

    private Long productoIdCreado;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("test-inversion@local", "pw",
                        List.of(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))));
    }

    @AfterEach
    void tearDown() {
        if (productoIdCreado != null) {
            movimientoInventarioRepository.findByProductoId(productoIdCreado)
                    .forEach(movimientoInventarioRepository::delete);
            productoRepository.deleteById(productoIdCreado);
            auditoriaRepository.findAll().stream()
                    .filter(a -> "PRODUCTO".equals(a.getEntidad())
                            && productoIdCreado.equals(a.getEntidadId()))
                    .forEach(auditoriaRepository::delete);
            productoIdCreado = null;
        }
        SecurityContextHolder.clearContext();
    }

    private ProductoRequest request(String referencia) {
        ProductoRequest request = new ProductoRequest();
        request.setNombre("Producto prueba inversion");
        request.setReferencia(referencia);
        request.setCategoriaId(categoriaRepository.findAll().get(0).getId());
        request.setPrecioCompra(PRECIO_COMPRA);
        request.setPrecioVenta(PRECIO_COMPRA * 2);
        request.setStockActual(STOCK_INICIAL);
        request.setStockMinimo(1);
        return request;
    }

    @Test
    void altaDeProductoConStockInicialCuentaComoCompraEnInversion() {
        String referencia = "TEST-INV-" + System.nanoTime();
        double inversionAntes = reporteService.obtenerEstadisticasInversionGlobal()
                .getInversionMercanciaPeriodo();

        ProductoResponse creado = productoService.crear(request(referencia));
        productoIdCreado = creado.getId();

        Optional<MovimientoInventarioEntity> movimiento =
                movimientoInventarioRepository.findByProductoId(creado.getId()).stream()
                        .filter(m -> m.getTipo() == TipoMovimiento.ENTRADA)
                        .findFirst();
        assertTrue(movimiento.isPresent(), "El alta debe generar un movimiento de ENTRADA");
        assertEquals(STOCK_INICIAL, movimiento.get().getCantidad());
        assertEquals(PRECIO_COMPRA, movimiento.get().getCostoUnitario());
        assertEquals(Boolean.TRUE, movimiento.get().getEsCompra());

        ProductoEntity producto = productoRepository.findById(creado.getId()).orElseThrow();
        assertEquals(STOCK_INICIAL, producto.getStockActual(),
                "El stock no debe duplicarse al registrar el movimiento");

        EstadisticasInversionResponse reporte = reporteService.obtenerEstadisticasInversionGlobal();
        assertEquals(inversionAntes + MONTO_ESPERADO,
                reporte.getInversionMercanciaPeriodo(), 0.01);

        InversionProductoDTO enReporte = reporte.getComprasPorProducto().stream()
                .filter(p -> referencia.equals(p.getReferencia()))
                .findFirst()
                .orElseThrow(() -> new AssertionError(
                        "El producto nuevo debe aparecer en compras por producto"));
        assertEquals((long) STOCK_INICIAL, enReporte.getCantidad());
        assertEquals(MONTO_ESPERADO, enReporte.getMonto(), 0.01);
    }
}
