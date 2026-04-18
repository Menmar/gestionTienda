package com.menmar.gestionTienda.model.cliente;

import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Schema(description = "Datos para crear o actualizar un cliente")
public record ClienteRequest(
        @Schema(description = "Nombre del cliente", example = "Carlos")
        @NotBlank @Size(max = 100) String nombre,

        @Schema(description = "Apellidos del cliente", example = "Martínez Ruiz")
        @Size(max = 150) String apellidos,

        @Schema(description = "Teléfono de contacto (único en el sistema)", example = "612345678")
        @NotBlank @Size(max = 20) String telefono,

        @Schema(description = "Email de contacto (opcional)", example = "carlos@email.com")
        @Size(max = 255) String email,

        @Schema(description = "Canales por los que el cliente quiere recibir notificaciones cuando su ticket esté listo",
                example = "[\"EMAIL\", \"WHATSAPP\"]",
                allowableValues = {"EMAIL", "WHATSAPP", "TELEGRAM"})
        Set<CanalNotificacion> canalesNotificacion,

        @Schema(description = "Chat ID de Telegram (necesario si TELEGRAM está en canalesNotificacion)", example = "123456789")
        @Size(max = 50) String telegramChatId
) {}
