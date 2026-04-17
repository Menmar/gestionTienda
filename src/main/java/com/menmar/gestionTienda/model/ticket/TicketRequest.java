package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TicketRequest(
        @NotNull TipoTicket tipo,
        @NotNull Long clienteId,
        LocalDate fechaPrevista,
        String observaciones,
        @Valid List<LineaCalzadoRequest> lineasCalzado,
        @Valid List<LineaCosturaRequest> lineasCostura,
        @Valid List<LineaLlaveRequest> lineasLlave
) {}
