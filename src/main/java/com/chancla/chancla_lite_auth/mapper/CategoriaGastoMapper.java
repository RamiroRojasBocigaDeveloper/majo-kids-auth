package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.CategoriaGastoRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaGastoResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaGastoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaGastoMapper {

    // Request → Entity
    CategoriaGastoEntity toEntity(CategoriaGastoRequest request);

    // Entity → Response
    CategoriaGastoResponse toResponse(CategoriaGastoEntity entity);

    // List<Entity> → List<Response>
    List<CategoriaGastoResponse> toResponseList(List<CategoriaGastoEntity> entities);

    // Actualizar Entity desde Request (ignora campos null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoriaGastoRequest request, @MappingTarget CategoriaGastoEntity entity);
}