package com.menmar.gestionTienda.model.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambioPasswordRequest(
        @NotBlank String passwordActual,
        @NotBlank @Size(min = 8) String passwordNueva
) {}
