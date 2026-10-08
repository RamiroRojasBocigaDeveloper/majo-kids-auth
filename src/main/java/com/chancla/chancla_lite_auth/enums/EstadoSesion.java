package com.chancla.chancla_lite_auth.enums;

import lombok.Getter;

@Getter
public enum EstadoSesion {
    ABIERTA("abierta"),
    CERRADA("cerrada");

    private final String valor;

    EstadoSesion(String valor) {
        this.valor = valor;
    }
}
