package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Línea de duplicado de llave dentro de un ticket")
public record LineaLlaveRequest(
        @Schema(description = "ID del tipo de llave del catálogo", example = "1")
        @NotNull Long tipoLlaveId,

        @Schema(description = "Número de copias (mínimo 1)", example = "2")
        @Min(1) short cantidad,

        @Schema(description = "Precio manual (si es null se usa el precio base o del establecimiento)", example = "4.00")
        @DecimalMin("0.01") BigDecimal precioManual,

        @Schema(description = "Descuento sobre esta línea (0-100%)", example = "0.00")
        @DecimalMin("0") @DecimalMax("100") BigDecimal descuento
) {}
