package com.menmar.gestionTienda.model.usuario;

import com.menmar.gestionTienda.persistence.entity.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Datos para crear o actualizar un usuario del sistema")
public record UsuarioRequest(
        @Schema(description = "Nombre", example = "María")
        @NotBlank @Size(max = 100) String nombre,

        @Schema(description = "Apellidos", example = "García López")
        @NotBlank @Size(max = 150) String apellidos,

        @Schema(description = "Email (identificador único)", example = "maria@tienda.com")
        @NotBlank @Email String email,

        @Schema(description = "Contraseña inicial (mínimo 8 caracteres). Solo se usa al crear; ignorada en actualizar.", example = "Temporal1!")
        @NotBlank @Size(min = 8) String passwordInicial,

        @Schema(description = "Rol del usuario", example = "EMPLEADO")
        @NotNull RolUsuario rol
) {}
