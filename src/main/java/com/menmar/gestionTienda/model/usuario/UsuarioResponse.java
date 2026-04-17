package com.menmar.gestionTienda.model.usuario;

import com.menmar.gestionTienda.persistence.entity.RolUsuario;

import java.time.OffsetDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String apellidos,
        String email,
        RolUsuario rol,
        boolean activo,
        OffsetDateTime createdAt
) {}
