-- Modulo de compras de mercancia (inversion / surtido)
-- No se relaciona con sesiones_trabajo ni con gastos: no afecta la caja del vendedor.
-- Ejecutar en local y, con aprobacion, en produccion.

CREATE TABLE IF NOT EXISTS compras_mercancia (
    id BIGINT NOT NULL AUTO_INCREMENT,
    fecha DATE NOT NULL,
    proveedor VARCHAR(150) DEFAULT NULL,
    descripcion VARCHAR(200) DEFAULT NULL,
    metodo_pago VARCHAR(20) DEFAULT NULL,
    monto_total DOUBLE NOT NULL,
    detallada TINYINT(1) NOT NULL DEFAULT 1,
    usuario_id BIGINT DEFAULT NULL,
    created_at DATETIME(6) DEFAULT NULL,
    PRIMARY KEY (id),
    KEY idx_compras_fecha (fecha),
    KEY fk_compras_usuario (usuario_id),
    CONSTRAINT fk_compras_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS detalles_compra_mercancia (
    id BIGINT NOT NULL AUTO_INCREMENT,
    compra_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    costo_unitario DOUBLE NOT NULL,
    subtotal DOUBLE NOT NULL,
    PRIMARY KEY (id),
    KEY idx_detalle_compra (compra_id),
    KEY fk_detalle_producto (producto_id),
    CONSTRAINT fk_detalle_compra FOREIGN KEY (compra_id) REFERENCES compras_mercancia (id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
