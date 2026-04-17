package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.auth.LoginRequest;
import com.menmar.gestionTienda.model.auth.LoginResponse;
import com.menmar.gestionTienda.model.auth.RefreshRequest;
import com.menmar.gestionTienda.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Login, renovación de token y cierre de sesión")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @SecurityRequirements
    @Operation(summary = "Obtener tokens JWT", description = "Autentica con email y contraseña y devuelve un access token (corto) y un refresh token (largo).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticación correcta"),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas")
    })
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    @SecurityRequirements
    @Operation(summary = "Renovar access token", description = "Intercambia un refresh token válido por un nuevo par de tokens.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renovados"),
            @ApiResponse(responseCode = "401", description = "Refresh token inválido o revocado")
    })
    public ResponseEntity<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Revoca el refresh token asociado al Bearer enviado en la cabecera Authorization.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sesión cerrada"),
            @ApiResponse(responseCode = "401", description = "Token no válido o ya revocado")
    })
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String auth) {
        authService.logout(auth);
        return ResponseEntity.noContent().build();
    }
}
