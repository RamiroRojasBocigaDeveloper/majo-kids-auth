package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.CategoriaRequest;
import com.chancla.chancla_lite_auth.dto.response.CategoriaResponse;
import com.chancla.chancla_lite_auth.entity.CategoriaEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

    // Request → Entity
    CategoriaEntity toEntity(CategoriaRequest request);

    // Entity → Response
    CategoriaResponse toResponse(CategoriaEntity entity);

    // List<Entity> → List<Response>
    List<CategoriaResponse> toResponseList(List<CategoriaEntity> entities);

    // Actualizar Entity desde Request (ignora campos null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(CategoriaRequest request, @MappingTarget CategoriaEntity entity);
}
