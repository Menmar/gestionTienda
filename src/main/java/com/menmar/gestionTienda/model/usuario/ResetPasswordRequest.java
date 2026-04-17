package com.menmar.gestionTienda.model.usuario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Nueva contraseña impuesta por el ADMIN (no requiere la contraseña actual)")
public record ResetPasswordRequest(
        @Schema(description = "Nueva contraseña (mínimo 8 caracteres)", example = "Reset1234!")
        @NotBlank @Size(min = 8) String passwordNueva
) {}
