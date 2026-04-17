package com.menmar.gestionTienda.model.ticket;

import java.math.BigDecimal;

public record LineaResponse(
        Long id,
        String tipoNombre,
        short cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        String descripcion
) {}
