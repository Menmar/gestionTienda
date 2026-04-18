package com.menmar.gestionTienda.model.catalogo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar una familia de reparación")
public record FamiliaReparacionRequest(
        @Schema(description = "Nombre de la familia", example = "Suelas")
        @NotBlank @Size(max = 100) String nombre
) {}
