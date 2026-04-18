package com.menmar.gestionTienda.model.cliente;

import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Set;

@Schema(description = "Datos de un cliente registrado en la tienda")
public record ClienteResponse(
        @Schema(description = "ID interno del cliente") Long id,
        @Schema(description = "Nombre") String nombre,
        @Schema(description = "Apellidos") String apellidos,
        @Schema(description = "Teléfono de contacto") String telefono,
        @Schema(description = "Email de contacto (puede ser nulo)") String email,
        @Schema(description = "Canales de notificación activos") Set<CanalNotificacion> canalesNotificacion,
        @Schema(description = "Fecha y hora de alta") OffsetDateTime createdAt
) {}
