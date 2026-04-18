package com.menmar.gestionTienda.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Refresh token necesario para renovar el access token")
public record RefreshRequest(
        @Schema(description = "Refresh token obtenido previamente en /auth/login")
        @NotBlank String refreshToken
) {}
