package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record TicketResponse(
        Long id,
        String numeroTicket,
        TipoTicket tipo,
        Long clienteId,
        String clienteNombre,
        String clienteTelefono,
        Long empleadoId,
        String empleadoNombre,
        EstadoTicket estado,
        BigDecimal precioTotal,
        LocalDate fechaEntrada,
        LocalDate fechaPrevista,
        LocalDate fechaEntrega,
        String observaciones,
        OffsetDateTime createdAt,
        List<LineaResponse> lineasCalzado,
        List<LineaResponse> lineasCostura,
        List<LineaResponse> lineasLlave
) {}
