package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear o actualizar un tipo de costura")
public record TipoCosturaRequest(
        @Schema(description = "Nombre del tipo de costura", example = "Costura a mano bolso")
        @NotBlank @Size(max = 150) String nombre,

        @Schema(description = "Precio base sin IVA", example = "8.00")
        @NotNull @DecimalMin("0.01") BigDecimal precioBase
) {}
