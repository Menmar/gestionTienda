package com.menmar.gestionTienda.model.usuario;

import com.menmar.gestionTienda.persistence.entity.RolUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Datos públicos de un usuario del sistema")
public record UsuarioResponse(
        @Schema(description = "ID interno del usuario") Long id,
        @Schema(description = "Nombre") String nombre,
        @Schema(description = "Apellidos") String apellidos,
        @Schema(description = "Email") String email,
        @Schema(description = "Rol asignado (ADMIN o EMPLEADO)") RolUsuario rol,
        @Schema(description = "Indica si el usuario puede acceder al sistema") boolean activo,
        @Schema(description = "Fecha y hora de creación") OffsetDateTime createdAt
) {}
