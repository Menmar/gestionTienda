package com.menmar.gestionTienda.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(Jwt jwt, Fotos fotos, Admin admin, Notificaciones notificaciones) {

    public record Jwt(String secret, long expirationMs, long refreshExpirationMs) {}

    public record Fotos(String directorio) {}

    public record Admin(String email, String nombre, String apellidos, String password) {}

    public record Notificaciones(String callmebotApiKey, String telegramBotToken) {}
}
