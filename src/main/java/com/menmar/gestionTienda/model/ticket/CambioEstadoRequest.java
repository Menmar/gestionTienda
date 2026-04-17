package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoRequest(@NotNull EstadoTicket estado) {}
