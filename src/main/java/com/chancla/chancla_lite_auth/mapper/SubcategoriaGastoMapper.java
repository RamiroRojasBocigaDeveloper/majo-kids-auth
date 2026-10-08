package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.SubcategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.SubcategoriaGastoResponse;
import com.chancla.chancla_lite_auth.entity.SubcategoriaGastoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SubcategoriaGastoMapper {

    @Mapping(target = "categoriaGasto", ignore = true)
    SubcategoriaGastoEntity toEntity(SubcategoriaGastoRequest request);

    @Mapping(source = "categoriaGasto.id", target = "categoriaGastoId")
    @Mapping(source = "categoriaGasto.nombre", target = "categoriaGastoNombre")
    SubcategoriaGastoResponse toResponse(SubcategoriaGastoEntity entity);

    List<SubcategoriaGastoResponse> toResponseList(List<SubcategoriaGastoEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoriaGasto", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(SubcategoriaGastoRequest request, @MappingTarget SubcategoriaGastoEntity entity);
}
