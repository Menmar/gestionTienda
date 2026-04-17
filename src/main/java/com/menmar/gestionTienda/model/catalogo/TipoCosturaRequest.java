package com.menmar.gestionTienda.model.catalogo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TipoCosturaRequest(
        @NotBlank @Size(max = 150) String nombre,
        @NotNull @DecimalMin("0.01") BigDecimal precioBase
) {}
