package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.auth.LoginRequest;
import com.menmar.gestionTienda.model.auth.LoginResponse;
import com.menmar.gestionTienda.model.auth.RefreshRequest;

/**
 * Gestiona el ciclo de vida de las sesiones JWT:
 * autenticación, renovación de tokens y cierre de sesión.
 */
public interface AuthService {

    /**
     * Autentica al usuario y devuelve un access token de corta duración
     * y un refresh token de larga duración.
     *
     * @param request credenciales (email + contraseña)
     * @return par de tokens JWT
     * @throws org.springframework.security.core.AuthenticationException si las credenciales son incorrectas
     */
    LoginResponse login(LoginRequest request);

    /**
     * Intercambia un refresh token válido y no revocado por un nuevo par de tokens.
     *
     * @param request refresh token vigente
     * @return nuevo par de tokens JWT
     * @throws io.jsonwebtoken.JwtException si el token está expirado, es inválido o ya fue revocado
     */
    LoginResponse refresh(RefreshRequest request);

    /**
     * Revoca el refresh token asociado al Bearer enviado en la cabecera Authorization.
     *
     * @param bearerToken valor completo de la cabecera {@code Authorization: Bearer <token>}
     */
    void logout(String bearerToken);
}
