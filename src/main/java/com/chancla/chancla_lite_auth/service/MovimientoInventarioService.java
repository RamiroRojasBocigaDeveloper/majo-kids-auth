package com.chancla.chancla_lite_auth.service;

import com.chancla.chancla_lite_auth.dto.request.MovimientoInventarioRequest;
import com.chancla.chancla_lite_auth.dto.response.MovimientoInventarioResponse;
import java.util.List;

public interface MovimientoInventarioService {

    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest request);

    List<MovimientoInventarioResponse> obtenerHistorialPorProducto(Long productoId);

    List<MovimientoInventarioResponse> obtenerTodoElHistorial();

    MovimientoInventarioResponse obtenerPorId(Long id);
}
