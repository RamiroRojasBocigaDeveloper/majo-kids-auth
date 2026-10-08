package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.VentaRequest;
import com.chancla.chancla_lite_auth.dto.response.VentaResponse;
import com.chancla.chancla_lite_auth.entity.DetalleVentaEntity;
import com.chancla.chancla_lite_auth.entity.VentaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    @Mapping(target = "sesion", ignore = true)
    @Mapping(target = "metodoPago", ignore = true)
    VentaEntity toEntity(VentaRequest request);

    @Mapping(source = "entity.sesion.id", target = "sesionId")
    @Mapping(source = "entity.metodoPago.id", target = "metodoPagoId")
    @Mapping(source = "entity.metodoPago.nombre", target = "metodoPagoNombre")
    @Mapping(source = "entity.sesion.usuario.nombre", target = "nombreVendedor")
    @Mapping(source = "entity.fechaRegistroManual", target = "fechaRegistroManual")
    VentaResponse toResponse(VentaEntity entity, List<VentaResponse.DetalleVentaResponse> detalles);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    @Mapping(source = "producto.referencia", target = "productoReferencia")
    @Mapping(source = "producto.categoria.nombre", target = "categoriaNombre")
    VentaResponse.DetalleVentaResponse toDetalleResponse(DetalleVentaEntity entity);

    List<VentaResponse.DetalleVentaResponse> toDetalleResponseList(List<DetalleVentaEntity> entities);
}
