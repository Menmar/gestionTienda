package com.menmar.gestionTienda.model.foto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Metadatos de una foto adjunta a un ticket")
public record FotoResponse(
        @Schema(description = "ID de la foto") Long id,
        @Schema(description = "ID del ticket al que pertenece") Long ticketId,
        @Schema(description = "Nombre del fichero almacenado en disco") String nombreFichero,
        @Schema(description = "Fecha y hora de subida") OffsetDateTime createdAt
) {}
