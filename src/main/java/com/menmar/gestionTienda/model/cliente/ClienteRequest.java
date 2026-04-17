package com.menmar.gestionTienda.model.cliente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 150) String apellidos,
        @NotBlank @Size(max = 20) String telefono,
        @Size(max = 255) String email
) {}
