package com.chancla.chancla_lite_auth.mapper;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import com.chancla.chancla_lite_auth.entity.ProductoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductoMapper {

    @Mapping(target = "categoria", ignore = true) // Se maneja en el Service
    ProductoEntity toEntity(ProductoRequest request);

    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    ProductoResponse toResponse(ProductoEntity entity);

    List<ProductoResponse> toResponseList(List<ProductoEntity> entities);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true) // Se maneja en el Service
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ProductoRequest request, @MappingTarget ProductoEntity entity);
}
