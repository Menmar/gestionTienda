package com.menmar.gestionTienda.persistence.entity;

import java.util.Set;

public enum EstadoTicket {

    PENDIENTE, EN_PROCESO, LISTO, ENTREGADO, CANCELADO;

    private Set<EstadoTicket> transicionesValidas;

    static {
        PENDIENTE .transicionesValidas = Set.of(EN_PROCESO, CANCELADO);
        EN_PROCESO.transicionesValidas = Set.of(LISTO,      CANCELADO);
        LISTO     .transicionesValidas = Set.of(ENTREGADO,  CANCELADO);
        ENTREGADO .transicionesValidas = Set.of();
        CANCELADO .transicionesValidas = Set.of();
    }

    public boolean puedeTransicionarA(EstadoTicket destino) {
        return transicionesValidas.contains(destino);
    }
}
