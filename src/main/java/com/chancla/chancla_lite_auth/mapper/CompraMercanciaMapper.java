package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.response.CompraMercanciaResponse;
import com.chancla.chancla_lite_auth.dto.response.DetalleCompraResponse;
import com.chancla.chancla_lite_auth.entity.CompraMercanciaEntity;
import com.chancla.chancla_lite_auth.entity.DetalleCompraMercanciaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompraMercanciaMapper {

    public CompraMercanciaResponse toResponse(CompraMercanciaEntity compra, List<DetalleCompraMercanciaEntity> detalles) {
        CompraMercanciaResponse response = new CompraMercanciaResponse();
        response.setId(compra.getId());
        response.setFecha(compra.getFecha());
        response.setProveedor(compra.getProveedor());
        response.setDescripcion(compra.getDescripcion());
        response.setMetodoPago(compra.getMetodoPago() != null ? compra.getMetodoPago().name() : null);
        response.setMontoTotal(compra.getMontoTotal());
        response.setDetallada(compra.getDetallada());
        response.setUsuarioNombre(compra.getUsuario() != null ? compra.getUsuario().getNombre() : null);
        response.setCreatedAt(compra.getCreatedAt());

        List<DetalleCompraResponse> lineas = new ArrayList<>();
        if (detalles != null) {
            for (DetalleCompraMercanciaEntity detalle : detalles) {
                lineas.add(toDetalle(detalle));
            }
        }
        response.setLineas(lineas);
        return response;
    }

    private DetalleCompraResponse toDetalle(DetalleCompraMercanciaEntity detalle) {
        DetalleCompraResponse response = new DetalleCompraResponse();
        response.setProductoId(detalle.getProducto().getId());
        response.setProductoNombre(detalle.getProducto().getNombre());
        response.setReferencia(detalle.getProducto().getReferencia());
        response.setCantidad(detalle.getCantidad());
        response.setCostoUnitario(detalle.getCostoUnitario());
        response.setSubtotal(detalle.getSubtotal());
        return response;
    }
}
