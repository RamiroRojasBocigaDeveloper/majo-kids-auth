package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.SueldoPagadoRequest;
import com.chancla.chancla_lite_auth.dto.response.SueldoPagadoResponse;
import com.chancla.chancla_lite_auth.entity.SueldoPagadoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SueldoPagadoMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "sesion", ignore = true)
    SueldoPagadoEntity toEntity(SueldoPagadoRequest request);

    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "usuario.nombre", target = "nombreUsuario")
    @Mapping(source = "sesion.id", target = "sesionId")
    SueldoPagadoResponse toResponse(SueldoPagadoEntity entity);

    List<SueldoPagadoResponse> toResponseList(List<SueldoPagadoEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "sesion", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(SueldoPagadoRequest request, @MappingTarget SueldoPagadoEntity entity);
}
