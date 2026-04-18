package com.menmar.gestionTienda.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Fotos fotos) {

    public record Jwt(String secret, long expirationMs, long refreshExpirationMs) {}

    public record Fotos(String directorio) {}
}
