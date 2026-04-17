package com.menmar.gestionTienda.model.foto;

import java.time.OffsetDateTime;

public record FotoResponse(
        Long id,
        Long ticketId,
        String nombreFichero,
        OffsetDateTime createdAt
) {}
