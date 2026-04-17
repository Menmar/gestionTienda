package com.menmar.gestionTienda.model.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para que el propio empleado cambie su contraseña")
public record CambioPasswordRequest(
        @Schema(description = "Contraseña actual del usuario", example = "Temporal1!")
        @NotBlank String passwordActual,

        @Schema(description = "Nueva contraseña (mínimo 8 caracteres)", example = "NuevaSegura2!")
        @NotBlank @Size(min = 8) String passwordNueva
) {}
