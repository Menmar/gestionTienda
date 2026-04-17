package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Línea de costura dentro de un ticket")
public record LineaCosturaRequest(
        @Schema(description = "ID del tipo de costura del catálogo", example = "2")
        @NotNull Long tipoCosturaId,

        @Schema(description = "Número de unidades (mínimo 1)", example = "1")
        @Min(1) short cantidad,

        @Schema(description = "Descripción adicional opcional", example = "Bolso de señora")
        String descripcion
) {}
