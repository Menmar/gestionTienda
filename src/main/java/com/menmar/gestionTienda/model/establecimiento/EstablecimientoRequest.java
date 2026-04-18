package com.menmar.gestionTienda.model.establecimiento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar un establecimiento")
public record EstablecimientoRequest(
        @Schema(description = "Nombre del establecimiento", example = "Reparaciones Centro")
        @NotBlank @Size(max = 150) String nombre,

        @Schema(description = "Dirección completa", example = "Calle Mayor 12, 28001 Madrid")
        @NotBlank @Size(max = 255) String direccion,

        @Schema(description = "Teléfono de contacto", example = "910123456")
        @Size(max = 20) String telefono
) {}
