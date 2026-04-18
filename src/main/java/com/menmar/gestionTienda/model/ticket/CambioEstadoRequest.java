package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Nuevo estado para el ticket")
public record CambioEstadoRequest(
        @Schema(description = "Estado destino (ABIERTO, EN_PROCESO, LISTO, ENTREGADO, CANCELADO)", example = "LISTO")
        @NotNull EstadoTicket estado
) {}
