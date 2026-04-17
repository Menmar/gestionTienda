package com.menmar.gestionTienda.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciales para iniciar sesión")
public record LoginRequest(
        @Schema(description = "Email del usuario", example = "admin@tienda.com")
        @NotBlank @Email String email,

        @Schema(description = "Contraseña del usuario", example = "Segura1234!")
        @NotBlank String password
) {}
