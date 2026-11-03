package com.chancla.chancla_lite_auth.dto.response;

import java.util.List;
import java.util.Map;

public class EstadisticasInversionResponse {

    private Double gastosOperativos;
    private Double inversionMercanciaPeriodo;
    private Double saldoInicialMercancia;
    private Double inversionMercanciaTotal;
    private Double totalInvertidoPeriodo;
    private Double totalInvertidoAcumulado;
    private Map<String, Double> gastosOperativosPorSubcategoria;
    private List<InversionProductoDTO> comprasPorProducto;
    private Integer productosConSaldoInicial;

    public EstadisticasInversionResponse() {
    }

    public Double getGastosOperativos() { return gastosOperativos; }
    public void setGastosOperativos(Double gastosOperativos) { this.gastosOperativos = gastosOperativos; }

    public Double getInversionMercanciaPeriodo() { return inversionMercanciaPeriodo; }
    public void setInversionMercanciaPeriodo(Double inversionMercanciaPeriodo) { this.inversionMercanciaPeriodo = inversionMercanciaPeriodo; }

    public Double getSaldoInicialMercancia() { return saldoInicialMercancia; }
    public void setSaldoInicialMercancia(Double saldoInicialMercancia) { this.saldoInicialMercancia = saldoInicialMercancia; }

    public Double getInversionMercanciaTotal() { return inversionMercanciaTotal; }
    public void setInversionMercanciaTotal(Double inversionMercanciaTotal) { this.inversionMercanciaTotal = inversionMercanciaTotal; }

    public Double getTotalInvertidoPeriodo() { return totalInvertidoPeriodo; }
    public void setTotalInvertidoPeriodo(Double totalInvertidoPeriodo) { this.totalInvertidoPeriodo = totalInvertidoPeriodo; }

    public Double getTotalInvertidoAcumulado() { return totalInvertidoAcumulado; }
    public void setTotalInvertidoAcumulado(Double totalInvertidoAcumulado) { this.totalInvertidoAcumulado = totalInvertidoAcumulado; }

    public Map<String, Double> getGastosOperativosPorSubcategoria() { return gastosOperativosPorSubcategoria; }
    public void setGastosOperativosPorSubcategoria(Map<String, Double> gastosOperativosPorSubcategoria) { this.gastosOperativosPorSubcategoria = gastosOperativosPorSubcategoria; }

    public List<InversionProductoDTO> getComprasPorProducto() { return comprasPorProducto; }
    public void setComprasPorProducto(List<InversionProductoDTO> comprasPorProducto) { this.comprasPorProducto = comprasPorProducto; }

    public Integer getProductosConSaldoInicial() { return productosConSaldoInicial; }
    public void setProductosConSaldoInicial(Integer productosConSaldoInicial) { this.productosConSaldoInicial = productosConSaldoInicial; }
}
