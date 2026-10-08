package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.UsuarioRequest;
import com.chancla.chancla_lite_auth.dto.response.UsuarioResponse;
import com.chancla.chancla_lite_auth.entity.UsuarioEntity;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", 
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UsuarioMapper {

    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "password", ignore = true)
    UsuarioEntity toEntity(UsuarioRequest request);

    @Mapping(source = "rol.nombre", target = "rolNombre")
    UsuarioResponse toResponse(UsuarioEntity entity);

    List<UsuarioResponse> toResponseList(List<UsuarioEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rol", ignore = true)
    @Mapping(target = "password", ignore = true)
    void updateEntityFromRequest(UsuarioRequest request, @MappingTarget UsuarioEntity entity);
}
