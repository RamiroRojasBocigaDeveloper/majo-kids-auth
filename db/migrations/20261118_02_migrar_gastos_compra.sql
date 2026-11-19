-- Migracion LOCAL de los gastos que en realidad eran compras de mercancia.
-- Gastos migrados: 333.700 (id 16), 178.000 (id 15), 148.200 (id 21), 60.000 (id 10).
-- NO se migra "Pago P" (id 11): no es mercancia.
-- Quedan como compras sin detalle (detallada = 0): suman en los KPIs financieros,
-- pero no generan movimientos de inventario porque no se conoce el producto/cantidad.

START TRANSACTION;

INSERT INTO compras_mercancia (fecha, proveedor, descripcion, metodo_pago, monto_total, detallada, usuario_id, created_at)
SELECT DATE(COALESCE(g.fecha_registro_manual, g.created_at)),
       NULL,
       CONCAT(g.descripcion, ' (migrada del gasto #', g.id, ')'),
       NULL,
       g.monto,
       0,
       NULL,
       NOW()
FROM gastos g
WHERE g.id IN (10, 15, 16, 21);

DELETE FROM gastos WHERE id IN (10, 15, 16, 21);

COMMIT;
