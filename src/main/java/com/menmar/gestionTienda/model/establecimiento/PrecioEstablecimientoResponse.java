package com.menmar.gestionTienda.model.establecimiento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Precio personalizado de un ítem para un establecimiento")
public record PrecioEstablecimientoResponse(
        Long id,
        Long establecimientoId,
        Long itemId,
        String itemNombre,
        BigDecimal precioBase,
        BigDecimal precioPersonalizado
) {}
