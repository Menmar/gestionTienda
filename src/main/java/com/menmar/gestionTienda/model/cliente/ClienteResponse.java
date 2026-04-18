package com.menmar.gestionTienda.model.cliente;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Datos de un cliente registrado en la tienda")
public record ClienteResponse(
        @Schema(description = "ID interno del cliente") Long id,
        @Schema(description = "Nombre") String nombre,
        @Schema(description = "Apellidos") String apellidos,
        @Schema(description = "Teléfono de contacto") String telefono,
        @Schema(description = "Email de contacto (puede ser nulo)") String email,
        @Schema(description = "Notificación por email activada") boolean notifEmail,
        @Schema(description = "Notificación por WhatsApp activada") boolean notifWhatsapp,
        @Schema(description = "Notificación por Telegram activada") boolean notifTelegram,
        @Schema(description = "Fecha y hora de alta") OffsetDateTime createdAt
) {}
