package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import com.chancla.chancla_lite_auth.entity.MovimientoInventarioEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovimientoInventarioMapper {

    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "tipo", ignore = true)
    MovimientoInventarioEntity toEntity(MovimientoInventarioRequest request);

    @Mapping(source = "producto.id", target = "productoId")
    @Mapping(source = "producto.nombre", target = "productoNombre")
    MovimientoInventarioResponse toResponse(MovimientoInventarioEntity entity);

    List<MovimientoInventarioResponse> toResponseList(List<MovimientoInventarioEntity> entities);
}
