package com.menmar.gestionTienda.model.establecimiento;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Precios disponibles para un ítem al crear un ticket")
public record PreciosSugeridosResponse(
        Long itemId,
        String itemNombre,
        BigDecimal precioBase,
        BigDecimal precioEstablecimiento
) {}
