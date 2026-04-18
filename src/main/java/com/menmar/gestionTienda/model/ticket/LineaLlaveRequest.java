package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Línea de duplicado de llave dentro de un ticket")
public record LineaLlaveRequest(
        @Schema(description = "ID del tipo de llave del catálogo", example = "1")
        @NotNull Long tipoLlaveId,

        @Schema(description = "Número de copias (mínimo 1)", example = "2")
        @Min(1) short cantidad
) {}
