package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.usuario.CambioPasswordRequest;
import com.menmar.gestionTienda.model.usuario.ResetPasswordRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioResponse;
import com.menmar.gestionTienda.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema (solo ADMIN, excepto cambio de contraseña propia)")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear usuario", description = "Crea un nuevo empleado o administrador. Requiere rol ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya existe")
    })
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Listar usuarios", description = "Devuelve todos los usuarios paginados, ordenados por apellidos.")
    @ApiResponse(responseCode = "200", description = "Listado de usuarios")
    public ResponseEntity<PageResponse<UsuarioResponse>> listar(
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("apellidos").ascending());
        return ResponseEntity.ok(usuarioService.listar(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> buscar(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar usuario", description = "Actualiza nombre, apellidos, email y rol. No modifica la contraseña.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> actualizar(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar usuario", description = "Marca al usuario como inactivo (soft delete). No elimina el registro.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario desactivado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> desactivar(
            @Parameter(description = "ID del usuario") @PathVariable Long id) {
        usuarioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Resetear contraseña (ADMIN)", description = "El administrador fuerza una nueva contraseña para cualquier usuario sin necesitar la actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contraseña actualizada"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> resetPassword(
            @Parameter(description = "ID del usuario") @PathVariable Long id,
            @Valid @RequestBody ResetPasswordRequest request) {
        usuarioService.resetPassword(id, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    @Operation(summary = "Cambiar contraseña propia", description = "El empleado autenticado cambia su propia contraseña. Requiere confirmar la contraseña actual.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contraseña cambiada"),
            @ApiResponse(responseCode = "400", description = "Contraseña actual incorrecta")
    })
    public ResponseEntity<Void> cambiarPassword(@AuthenticationPrincipal UserDetails principal,
                                                @Valid @RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(principal.getUsername(), request);
        return ResponseEntity.noContent().build();
    }
}
