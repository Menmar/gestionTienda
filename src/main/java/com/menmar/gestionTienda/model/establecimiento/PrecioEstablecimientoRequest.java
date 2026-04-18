package com.menmar.gestionTienda.model.establecimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Precio personalizado de un ítem del catálogo para un establecimiento")
public record PrecioEstablecimientoRequest(
        @Schema(description = "ID del ítem del catálogo (tipo reparación, costura o llave)", example = "3")
        @NotNull Long itemId,

        @Schema(description = "Precio personalizado", example = "5.50")
        @NotNull @DecimalMin("0.01") BigDecimal precio
) {}
