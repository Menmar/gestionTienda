package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Línea de reparación de calzado dentro de un ticket")
public record LineaCalzadoRequest(
        @Schema(description = "ID del tipo de reparación del catálogo", example = "3")
        @NotNull Long tipoReparacionId,

        @Schema(description = "Número de unidades (mínimo 1)", example = "1")
        @Min(1) short cantidad,

        @Schema(description = "Precio manual (si es null se usa el precio base o del establecimiento)", example = "14.00")
        @DecimalMin("0.01") BigDecimal precioManual,

        @Schema(description = "Descuento sobre esta línea (0-100%)", example = "0.00")
        @DecimalMin("0") @DecimalMax("100") BigDecimal descuento,

        @Schema(description = "Descripción adicional opcional", example = "Color marrón, talla 42")
        String descripcion
) {}
