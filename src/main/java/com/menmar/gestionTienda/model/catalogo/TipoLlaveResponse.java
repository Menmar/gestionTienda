package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Tipo de llave del catálogo")
public record TipoLlaveResponse(
        @Schema(description = "ID del tipo") Long id,
        @Schema(description = "Nombre", example = "Llave plana estándar") String nombre,
        @Schema(description = "Precio base por copia", example = "2.50") BigDecimal precioBase,
        @Schema(description = "Activo en el catálogo") boolean activo
) {}
