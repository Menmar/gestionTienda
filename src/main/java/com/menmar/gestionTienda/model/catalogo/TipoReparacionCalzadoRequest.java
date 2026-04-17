package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear o actualizar un tipo de reparación de calzado")
public record TipoReparacionCalzadoRequest(
        @Schema(description = "Nombre del tipo de reparación", example = "Media suela cuero")
        @NotBlank @Size(max = 150) String nombre,

        @Schema(description = "Precio base sin IVA", example = "12.50")
        @NotNull @DecimalMin("0.01") BigDecimal precioBase,

        @Schema(description = "ID de la familia a la que pertenece", example = "1")
        @NotNull Long familiaId
) {}
