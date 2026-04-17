package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Datos para abrir un nuevo ticket")
public record TicketRequest(
        @Schema(description = "Tipo de ticket", example = "CALZADO")
        @NotNull TipoTicket tipo,

        @Schema(description = "ID del cliente propietario", example = "1")
        @NotNull Long clienteId,

        @Schema(description = "Fecha prevista de entrega (opcional)", example = "2024-02-15")
        LocalDate fechaPrevista,

        @Schema(description = "Observaciones internas (opcional)", example = "El cliente quiere tinte negro")
        String observaciones,

        @Schema(description = "Líneas de reparación de calzado (puede estar vacío o ser nulo)")
        @Valid List<LineaCalzadoRequest> lineasCalzado,

        @Schema(description = "Líneas de costura (puede estar vacío o ser nulo)")
        @Valid List<LineaCosturaRequest> lineasCostura,

        @Schema(description = "Líneas de duplicado de llave (puede estar vacío o ser nulo)")
        @Valid List<LineaLlaveRequest> lineasLlave
) {}
