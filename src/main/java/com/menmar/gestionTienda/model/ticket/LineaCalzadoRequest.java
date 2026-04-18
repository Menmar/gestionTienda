package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Línea de reparación de calzado dentro de un ticket")
public record LineaCalzadoRequest(
        @Schema(description = "ID del tipo de reparación del catálogo", example = "3")
        @NotNull Long tipoReparacionId,

        @Schema(description = "Número de unidades (mínimo 1)", example = "1")
        @Min(1) short cantidad,

        @Schema(description = "Descripción adicional opcional", example = "Color marrón, talla 42")
        String descripcion
) {}
