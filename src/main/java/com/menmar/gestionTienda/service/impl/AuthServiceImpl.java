package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.config.AppProperties;
import com.menmar.gestionTienda.model.auth.LoginRequest;
import com.menmar.gestionTienda.model.auth.LoginResponse;
import com.menmar.gestionTienda.model.auth.RefreshRequest;
import com.menmar.gestionTienda.persistence.entity.Token;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import com.menmar.gestionTienda.persistence.repository.TokenRepository;
import com.menmar.gestionTienda.persistence.repository.UsuarioRepository;
import com.menmar.gestionTienda.security.JwtService;
import com.menmar.gestionTienda.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        tokenRepository.revocarTokensDeUsuario(usuario.getId());

        String accessToken = jwtService.generarToken(usuario);
        String refreshToken = jwtService.generarRefreshToken(usuario);

        tokenRepository.save(Token.builder()
                .usuario(usuario)
                .token(refreshToken)
                .expiracion(OffsetDateTime.now().plusSeconds(appProperties.jwt().refreshExpirationMs() / 1000))
                .revocado(false)
                .build());

        return new LoginResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public LoginResponse refresh(RefreshRequest request) {
        Token storedToken = tokenRepository.findByToken(request.refreshToken())
                .filter(t -> !t.isRevocado())
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido o revocado"));

        Usuario usuario = storedToken.getUsuario();

        if (!jwtService.esValido(request.refreshToken(), usuario)) {
            throw new IllegalArgumentException("Refresh token expirado");
        }

        storedToken.setRevocado(true);
        tokenRepository.save(storedToken);

        String newAccessToken = jwtService.generarToken(usuario);
        String newRefreshToken = jwtService.generarRefreshToken(usuario);

        tokenRepository.save(Token.builder()
                .usuario(usuario)
                .token(newRefreshToken)
                .expiracion(OffsetDateTime.now().plusSeconds(appProperties.jwt().refreshExpirationMs() / 1000))
                .revocado(false)
                .build());

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) return;
        String jwt = bearerToken.substring(7);
        tokenRepository.findByToken(jwt).ifPresent(t -> {
            t.setRevocado(true);
            tokenRepository.save(t);
        });
    }
}
