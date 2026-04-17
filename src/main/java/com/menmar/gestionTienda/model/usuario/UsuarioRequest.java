package com.menmar.gestionTienda.model.usuario;

import com.menmar.gestionTienda.persistence.entity.RolUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequest(
        @NotBlank @Size(max = 100) String nombre,
        @NotBlank @Size(max = 150) String apellidos,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password,
        @NotNull RolUsuario rol
) {}
