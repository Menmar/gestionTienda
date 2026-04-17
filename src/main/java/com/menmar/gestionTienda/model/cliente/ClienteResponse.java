package com.menmar.gestionTienda.model.cliente;

import java.time.OffsetDateTime;

public record ClienteResponse(
        Long id,
        String nombre,
        String apellidos,
        String telefono,
        String email,
        OffsetDateTime createdAt
) {}
