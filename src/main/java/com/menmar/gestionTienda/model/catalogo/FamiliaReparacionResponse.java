package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Familia de reparación del catálogo")
public record FamiliaReparacionResponse(
        @Schema(description = "ID de la familia") Long id,
        @Schema(description = "Nombre de la familia", example = "Suelas") String nombre,
        @Schema(description = "Indica si la familia está activa en el catálogo") boolean activo
) {}
