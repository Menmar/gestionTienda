package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Datos para crear o actualizar un tipo de llave")
public record TipoLlaveRequest(
        @Schema(description = "Nombre del tipo de llave", example = "Llave plana estándar")
        @NotBlank @Size(max = 150) String nombre,

        @Schema(description = "Precio base sin IVA por copia", example = "2.50")
        @NotNull @DecimalMin("0.01") BigDecimal precioBase
) {}
