package com.menmar.gestionTienda.security;

import com.menmar.gestionTienda.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppProperties appProperties;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(appProperties.jwt().secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(UserDetails user) {
        return buildToken(Map.of(), user, appProperties.jwt().expirationMs());
    }

    public String generarRefreshToken(UserDetails user) {
        return buildToken(Map.of(), user, appProperties.jwt().refreshExpirationMs());
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails user, long expMs) {
        var now = Instant.now();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expMs)))
                .signWith(key())
                .compact();
    }

    public String extraerEmail(String token) {
        return extraerClaim(token, Claims::getSubject);
    }

    public boolean esValido(String token, UserDetails user) {
        return extraerEmail(token).equals(user.getUsername()) && !estaExpirado(token);
    }

    private boolean estaExpirado(String token) {
        return extraerClaim(token, Claims::getExpiration).before(new Date());
    }

    public <T> T extraerClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(parsearClaims(token));
    }

    private Claims parsearClaims(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
