package com.chancla.chancla_lite_auth.dto.response;

import java.util.Map;
import java.util.List;

public class DashboardResponse {

    private Double totalVentas;
    private Double totalGastos;
    private Double totalSueldos;
    private Double gananciaNeta;
    private Long cantidadVentas;
    private Double gananciaProductos;
    private Double costoMercancia;
    private Map<String, Double> ventasPorMetodoPago;
    private Map<String, Double> ventasPorCategoria;
    private Map<String, Double> gananciasPorCategoria;
    private Map<String, Double> gastosPorCategoria;
    private Map<String, Long> productosMasVendidos;
    private Map<String, Map<String, Double>> gastosDesglosados;
    private List<VentaResponse> detalleVentas;
    private List<GastoResponse> detalleGastos;

    public DashboardResponse() {
    }

    // Getters y Setters
    public Double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(Double totalVentas) { this.totalVentas = totalVentas; }

    public Double getTotalGastos() { return totalGastos; }
    public void setTotalGastos(Double totalGastos) { this.totalGastos = totalGastos; }

    public Double getTotalSueldos() { return totalSueldos; }
    public void setTotalSueldos(Double totalSueldos) { this.totalSueldos = totalSueldos; }

    public Double getGananciaNeta() { return gananciaNeta; }
    public void setGananciaNeta(Double gananciaNeta) { this.gananciaNeta = gananciaNeta; }

    public Long getCantidadVentas() { return cantidadVentas; }
    public void setCantidadVentas(Long cantidadVentas) { this.cantidadVentas = cantidadVentas; }

    public Map<String, Double> getVentasPorMetodoPago() { return ventasPorMetodoPago; }
    public void setVentasPorMetodoPago(Map<String, Double> ventasPorMetodoPago) { this.ventasPorMetodoPago = ventasPorMetodoPago; }

    public Map<String, Double> getVentasPorCategoria() { return ventasPorCategoria; }
    public void setVentasPorCategoria(Map<String, Double> ventasPorCategoria) { this.ventasPorCategoria = ventasPorCategoria; }

    public Map<String, Double> getGananciasPorCategoria() { return gananciasPorCategoria; }
    public void setGananciasPorCategoria(Map<String, Double> gananciasPorCategoria) { this.gananciasPorCategoria = gananciasPorCategoria; }

    public Map<String, Double> getGastosPorCategoria() { return gastosPorCategoria; }
    public void setGastosPorCategoria(Map<String, Double> gastosPorCategoria) { this.gastosPorCategoria = gastosPorCategoria; }

    public Map<String, Long> getProductosMasVendidos() { return productosMasVendidos; }
    public void setProductosMasVendidos(Map<String, Long> productosMasVendidos) { this.productosMasVendidos = productosMasVendidos; }

    public Double getGananciaProductos() { return gananciaProductos; }
    public void setGananciaProductos(Double gananciaProductos) { this.gananciaProductos = gananciaProductos; }

    public Double getCostoMercancia() { return costoMercancia; }
    public void setCostoMercancia(Double costoMercancia) { this.costoMercancia = costoMercancia; }

    public Map<String, Map<String, Double>> getGastosDesglosados() { return gastosDesglosados; }
    public void setGastosDesglosados(Map<String, Map<String, Double>> gastosDesglosados) { this.gastosDesglosados = gastosDesglosados; }

    public List<VentaResponse> getDetalleVentas() { return detalleVentas; }
    public void setDetalleVentas(List<VentaResponse> detalleVentas) { this.detalleVentas = detalleVentas; }

    public List<GastoResponse> getDetalleGastos() { return detalleGastos; }
    public void setDetalleGastos(List<GastoResponse> detalleGastos) { this.detalleGastos = detalleGastos; }
}
