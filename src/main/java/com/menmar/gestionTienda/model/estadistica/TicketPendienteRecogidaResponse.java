package com.menmar.gestionTienda.model.estadistica;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Ticket listo para recoger con más de X días de espera")
public record TicketPendienteRecogidaResponse(
        Long id,
        String numeroTicket,
        String clienteNombre,
        String clienteTelefono,
        String establecimientoNombre,
        LocalDate fechaEntrada,
        LocalDate fechaPrevista,
        long diasEspera
) {}
