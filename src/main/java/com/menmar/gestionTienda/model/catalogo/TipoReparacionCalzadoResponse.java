package com.menmar.gestionTienda.model.catalogo;

import java.math.BigDecimal;

public record TipoReparacionCalzadoResponse(
        Long id,
        String nombre,
        BigDecimal precioBase,
        Long familiaId,
        String familiaNombre,
        boolean activo
) {}
