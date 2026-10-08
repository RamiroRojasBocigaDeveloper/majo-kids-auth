package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.ProductoRequest;
import com.chancla.chancla_lite_auth.dto.response.ProductoResponse;
import java.util.List;

public interface ProductoService {

    List<ProductoResponse> obtenerTodos();

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse obtenerPorReferencia(String referencia);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);

    List<ProductoResponse> buscarPorNombre(String nombre);

    List<ProductoResponse> buscarPorCategoria(Integer categoriaId);

    List<ProductoResponse> obtenerStockBajo();
}
