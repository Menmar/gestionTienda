package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Tipo de reparación de calzado del catálogo")
public record TipoReparacionCalzadoResponse(
        @Schema(description = "ID del tipo") Long id,
        @Schema(description = "Nombre", example = "Media suela cuero") String nombre,
        @Schema(description = "Precio base", example = "12.50") BigDecimal precioBase,
        @Schema(description = "ID de la familia") Long familiaId,
        @Schema(description = "Nombre de la familia", example = "Suelas") String familiaNombre,
        @Schema(description = "Activo en el catálogo") boolean activo
) {}
