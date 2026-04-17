package com.menmar.gestionTienda.model.catalogo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FamiliaReparacionRequest(@NotBlank @Size(max = 100) String nombre) {}
