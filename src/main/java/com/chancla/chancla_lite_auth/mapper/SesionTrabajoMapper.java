package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.SesionTrabajoRequest;
import com.chancla.chancla_lite_auth.dto.response.SesionTrabajoResponse;
import com.chancla.chancla_lite_auth.entity.SesionTrabajoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SesionTrabajoMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    SesionTrabajoEntity toEntity(SesionTrabajoRequest request);

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "estado", target = "estado")
    SesionTrabajoResponse toResponse(SesionTrabajoEntity entity);

    List<SesionTrabajoResponse> toResponseList(List<SesionTrabajoEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(SesionTrabajoRequest request, @MappingTarget SesionTrabajoEntity entity);
}
