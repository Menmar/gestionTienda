package com.menmar.gestionTienda.persistence.entity;

public enum TipoTicket {

    CALZADO("CAL", "seq_ticket_calzado", "Reparación calzado"),
    COSTURA("COS", "seq_ticket_costura", "Costura"),
    LLAVE  ("LLA", "seq_ticket_llave",   "Duplicado de llaves");

    public final String prefijo;
    public final String secuencia;
    public final String etiqueta;

    TipoTicket(String prefijo, String secuencia, String etiqueta) {
        this.prefijo   = prefijo;
        this.secuencia = secuencia;
        this.etiqueta  = etiqueta;
    }
}
