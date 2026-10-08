package com.chancla.chancla_lite_auth.enums;

import lombok.Getter;

@Getter
public enum TipoMovimiento {
    ENTRADA("entrada"),
    SALIDA("salida"),
    AJUSTE("ajuste");

    private final String valor;

    TipoMovimiento(String valor) {
        this.valor = valor;
    }
}
