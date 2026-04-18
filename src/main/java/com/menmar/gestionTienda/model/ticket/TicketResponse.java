package com.menmar.gestionTienda.model.ticket;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Datos completos de un ticket de reparación o duplicado de llaves")
public record TicketResponse(
        @Schema(description = "ID interno del ticket") Long id,
        @Schema(description = "Código alfanumérico impreso en el resguardo", example = "TK-20240101-0001") String numeroTicket,
        @Schema(description = "Tipo de trabajo (CALZADO, COSTURA, LLAVE)") TipoTicket tipo,
        @Schema(description = "ID del cliente") Long clienteId,
        @Schema(description = "Nombre completo del cliente") String clienteNombre,
        @Schema(description = "Teléfono del cliente") String clienteTelefono,
        @Schema(description = "ID del empleado que abrió el ticket") Long empleadoId,
        @Schema(description = "Nombre del empleado") String empleadoNombre,
        @Schema(description = "Estado actual del ticket") EstadoTicket estado,
        @Schema(description = "Precio total calculado a partir de las líneas") BigDecimal precioTotal,
        @Schema(description = "Fecha de apertura del ticket") LocalDate fechaEntrada,
        @Schema(description = "Fecha prevista de entrega") LocalDate fechaPrevista,
        @Schema(description = "Fecha real de entrega al cliente") LocalDate fechaEntrega,
        @Schema(description = "Observaciones internas") String observaciones,
        @Schema(description = "Timestamp de creación en base de datos") OffsetDateTime createdAt,
        @Schema(description = "Líneas de reparación de calzado") List<LineaResponse> lineasCalzado,
        @Schema(description = "Líneas de costura") List<LineaResponse> lineasCostura,
        @Schema(description = "Líneas de duplicado de llave") List<LineaResponse> lineasLlave,
        @Schema(description = "Fotos adjuntas al ticket") List<FotoResponse> fotos
) {}
