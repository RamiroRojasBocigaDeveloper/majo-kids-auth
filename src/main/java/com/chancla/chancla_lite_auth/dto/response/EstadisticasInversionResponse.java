package com.chancla.chancla_lite_auth.dto.response;

import java.util.List;
import java.util.Map;

public class EstadisticasInversionResponse {

    private Double gastosOperativos;
    private Double inversionMercanciaPeriodo;
    private Double comprasRegistradas;
    private Double entradasIniciales;
    private Double saldoInicialMercancia;
    private Double inversionMercanciaTotal;
    private Double totalInvertidoPeriodo;
    private Double totalInvertidoAcumulado;
    private Double valorInventarioActual;
    private Long unidadesInventarioActual;
    private Integer productosConStock;
    private Map<String, Double> gastosOperativosPorSubcategoria;
    private List<InversionProductoDTO> comprasPorProducto;
    private List<InversionProductoDTO> entradasInicialesPorProducto;
    private Integer productosConSaldoInicial;

    public EstadisticasInversionResponse() {
    }

    public Double getGastosOperativos() { return gastosOperativos; }
    public void setGastosOperativos(Double gastosOperativos) { this.gastosOperativos = gastosOperativos; }

    public Double getInversionMercanciaPeriodo() { return inversionMercanciaPeriodo; }
    public void setInversionMercanciaPeriodo(Double inversionMercanciaPeriodo) { this.inversionMercanciaPeriodo = inversionMercanciaPeriodo; }

    public Double getComprasRegistradas() { return comprasRegistradas; }
    public void setComprasRegistradas(Double comprasRegistradas) { this.comprasRegistradas = comprasRegistradas; }

    public Double getEntradasIniciales() { return entradasIniciales; }
    public void setEntradasIniciales(Double entradasIniciales) { this.entradasIniciales = entradasIniciales; }

    public Double getSaldoInicialMercancia() { return saldoInicialMercancia; }
    public void setSaldoInicialMercancia(Double saldoInicialMercancia) { this.saldoInicialMercancia = saldoInicialMercancia; }

    public Double getInversionMercanciaTotal() { return inversionMercanciaTotal; }
    public void setInversionMercanciaTotal(Double inversionMercanciaTotal) { this.inversionMercanciaTotal = inversionMercanciaTotal; }

    public Double getTotalInvertidoPeriodo() { return totalInvertidoPeriodo; }
    public void setTotalInvertidoPeriodo(Double totalInvertidoPeriodo) { this.totalInvertidoPeriodo = totalInvertidoPeriodo; }

    public Double getTotalInvertidoAcumulado() { return totalInvertidoAcumulado; }
    public void setTotalInvertidoAcumulado(Double totalInvertidoAcumulado) { this.totalInvertidoAcumulado = totalInvertidoAcumulado; }

    public Double getValorInventarioActual() { return valorInventarioActual; }
    public void setValorInventarioActual(Double valorInventarioActual) { this.valorInventarioActual = valorInventarioActual; }

    public Long getUnidadesInventarioActual() { return unidadesInventarioActual; }
    public void setUnidadesInventarioActual(Long unidadesInventarioActual) { this.unidadesInventarioActual = unidadesInventarioActual; }

    public Integer getProductosConStock() { return productosConStock; }
    public void setProductosConStock(Integer productosConStock) { this.productosConStock = productosConStock; }

    public Map<String, Double> getGastosOperativosPorSubcategoria() { return gastosOperativosPorSubcategoria; }
    public void setGastosOperativosPorSubcategoria(Map<String, Double> gastosOperativosPorSubcategoria) { this.gastosOperativosPorSubcategoria = gastosOperativosPorSubcategoria; }

    public List<InversionProductoDTO> getComprasPorProducto() { return comprasPorProducto; }
    public void setComprasPorProducto(List<InversionProductoDTO> comprasPorProducto) { this.comprasPorProducto = comprasPorProducto; }

    public List<InversionProductoDTO> getEntradasInicialesPorProducto() { return entradasInicialesPorProducto; }
    public void setEntradasInicialesPorProducto(List<InversionProductoDTO> entradasInicialesPorProducto) { this.entradasInicialesPorProducto = entradasInicialesPorProducto; }

    public Integer getProductosConSaldoInicial() { return productosConSaldoInicial; }
    public void setProductosConSaldoInicial(Integer productosConSaldoInicial) { this.productosConSaldoInicial = productosConSaldoInicial; }
}
