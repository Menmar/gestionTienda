package com.menmar.gestionTienda.model.establecimiento;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de un establecimiento")
public record EstablecimientoResponse(
        Long id,
        String nombre,
        String direccion,
        String telefono,
        boolean activo
) {}
