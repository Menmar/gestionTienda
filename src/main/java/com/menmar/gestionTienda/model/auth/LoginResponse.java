package com.menmar.gestionTienda.model.auth;

public record LoginResponse(
        String accessToken,
        String refreshToken
) {}
