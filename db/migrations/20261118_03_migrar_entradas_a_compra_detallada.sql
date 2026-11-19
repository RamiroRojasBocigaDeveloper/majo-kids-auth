-- Migracion de las 4 entradas de stock como 4 COMPRAS INDIVIDUALES DETALLADAS
-- Permite ver cada compra por separado con su producto, cantidad, costo unitario y total.

START TRANSACTION;

-- Limpiar la compra agrupada anterior si existe
DELETE FROM detalles_compra_mercancia WHERE compra_id IN (SELECT id FROM compras_mercancia WHERE id >= 8);
DELETE FROM compras_mercancia WHERE id >= 8;

-- 1. Compra 1: Balaca tela delgada (Movimiento 195, Producto 75)
INSERT INTO compras_mercancia (fecha, proveedor, descripcion, metodo_pago, monto_total, detallada, usuario_id, created_at)
VALUES ('2026-11-07', 'Proveedor General', 'Compra Balaca tela delgada (Ref: AG010701)', 'TRANSFERENCIA', 28800.0, 1, NULL, '2026-11-07 13:57:09');
SET @c1 = LAST_INSERT_ID();

INSERT INTO detalles_compra_mercancia (compra_id, producto_id, cantidad, costo_unitario, subtotal)
VALUES (@c1, 75, 48, 600.0, 28800.0);

UPDATE movimientos_inventario
SET referencia_id = @c1, motivo = CONCAT('Compra de mercancia #', @c1, ' - Balaca tela delgada')
WHERE id = 195;

-- 2. Compra 2: Pinza moño satinado textura (Movimiento 220, Producto 111)
INSERT INTO compras_mercancia (fecha, proveedor, descripcion, metodo_pago, monto_total, detallada, usuario_id, created_at)
VALUES ('2026-11-07', 'Proveedor General', 'Compra Pinza moño satinado textura (Ref: AG070702)', 'TRANSFERENCIA', 36000.0, 1, NULL, '2026-11-07 14:47:15');
SET @c2 = LAST_INSERT_ID();

INSERT INTO detalles_compra_mercancia (compra_id, producto_id, cantidad, costo_unitario, subtotal)
VALUES (@c2, 111, 12, 3000.0, 36000.0);

UPDATE movimientos_inventario
SET referencia_id = @c2, motivo = CONCAT('Compra de mercancia #', @c2, ' - Pinza moño satinado textura')
WHERE id = 220;

-- 3. Compra 3: Pinza moño satinado (Movimiento 221, Producto 110)
INSERT INTO compras_mercancia (fecha, proveedor, descripcion, metodo_pago, monto_total, detallada, usuario_id, created_at)
VALUES ('2026-11-07', 'Proveedor General', 'Compra Pinza moño satinado (Ref: AG071302)', 'TRANSFERENCIA', 36000.0, 1, NULL, '2026-11-07 14:43:39');
SET @c3 = LAST_INSERT_ID();

INSERT INTO detalles_compra_mercancia (compra_id, producto_id, cantidad, costo_unitario, subtotal)
VALUES (@c3, 110, 12, 3000.0, 36000.0);

UPDATE movimientos_inventario
SET referencia_id = @c3, motivo = CONCAT('Compra de mercancia #', @c3, ' - Pinza moño satinado')
WHERE id = 221;

-- 4. Compra 4: Balaca y pinza Navideña (Movimiento 222, Producto 112)
INSERT INTO compras_mercancia (fecha, proveedor, descripcion, metodo_pago, monto_total, detallada, usuario_id, created_at)
VALUES ('2026-11-07', 'Proveedor General', 'Compra Balaca y pinza Navideña (Ref: NAV)', 'TRANSFERENCIA', 756000.0, 1, NULL, '2026-11-07 14:48:48');
SET @c4 = LAST_INSERT_ID();

INSERT INTO detalles_compra_mercancia (compra_id, producto_id, cantidad, costo_unitario, subtotal)
VALUES (@c4, 112, 216, 3500.0, 756000.0);

UPDATE movimientos_inventario
SET referencia_id = @c4, motivo = CONCAT('Compra de mercancia #', @c4, ' - Balaca y pinza Navideña')
WHERE id = 222;

COMMIT;