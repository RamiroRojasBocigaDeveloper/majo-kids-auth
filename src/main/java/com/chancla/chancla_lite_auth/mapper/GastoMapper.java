package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.GastoRequest;
import com.chancla.chancla_lite_auth.dto.response.GastoResponse;
import com.chancla.chancla_lite_auth.entity.GastoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GastoMapper {

    @Mapping(target = "sesion", ignore = true)
    @Mapping(target = "categoriaGasto", ignore = true)
    @Mapping(target = "subcategoriaGasto", ignore = true)
    GastoEntity toEntity(GastoRequest request);

    @Mapping(source = "sesion.id", target = "sesionId")
    @Mapping(source = "categoriaGasto.id", target = "categoriaGastoId")
    @Mapping(source = "categoriaGasto.nombre", target = "categoriaGastoNombre")
    @Mapping(source = "subcategoriaGasto.id", target = "subcategoriaGastoId")
    @Mapping(source = "subcategoriaGasto.nombre", target = "subcategoriaGastoNombre")
    @Mapping(source = "fechaRegistroManual", target = "fechaRegistroManual")
    @Mapping(source = "sesion.usuario.nombre", target = "nombreUsuario")
    GastoResponse toResponse(GastoEntity entity);

    List<GastoResponse> toResponseList(List<GastoEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sesion", ignore = true)
    @Mapping(target = "categoriaGasto", ignore = true)
    @Mapping(target = "subcategoriaGasto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(GastoRequest request, @MappingTarget GastoEntity entity);
}
