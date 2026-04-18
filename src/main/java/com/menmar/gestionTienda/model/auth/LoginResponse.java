package com.menmar.gestionTienda.model.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Par de tokens JWT devuelto tras autenticación o renovación")
public record LoginResponse(
        @Schema(description = "Access token de corta duración para incluir en Authorization: Bearer")
        String accessToken,

        @Schema(description = "Refresh token de larga duración para obtener nuevos access tokens")
        String refreshToken
) {}
