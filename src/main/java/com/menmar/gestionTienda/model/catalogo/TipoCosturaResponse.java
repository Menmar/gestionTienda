package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Tipo de costura del catálogo")
public record TipoCosturaResponse(
        @Schema(description = "ID del tipo") Long id,
        @Schema(description = "Nombre", example = "Costura a mano bolso") String nombre,
        @Schema(description = "Precio base", example = "8.00") BigDecimal precioBase,
        @Schema(description = "Activo en el catálogo") boolean activo
) {}
