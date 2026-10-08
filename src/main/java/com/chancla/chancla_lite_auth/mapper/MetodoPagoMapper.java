package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.MetodoPagoRequest;
import com.chancla.chancla_lite_auth.dto.response.MetodoPagoResponse;
import com.chancla.chancla_lite_auth.entity.MetodoPagoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MetodoPagoMapper {
    MetodoPagoEntity toEntity(MetodoPagoRequest request);
    MetodoPagoResponse toResponse(MetodoPagoEntity entity);
    List<MetodoPagoResponse> toResponseList(List<MetodoPagoEntity> entities);

    void updateEntityFromRequest(MetodoPagoRequest request, @MappingTarget MetodoPagoEntity entity);
}
