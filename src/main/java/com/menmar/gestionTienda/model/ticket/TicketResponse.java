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
        @Schema(description = "Código alfanumérico impreso en el resguardo", example = "CAL-00001") String numeroTicket,
        @Schema(description = "Tipo de trabajo (CALZADO, COSTURA, LLAVE)") TipoTicket tipo,
        @Schema(description = "ID del cliente") Long clienteId,
        @Schema(description = "Nombre completo del cliente") String clienteNombre,
        @Schema(description = "Teléfono del cliente") String clienteTelefono,
        @Schema(description = "ID del empleado que abrió el ticket") Long empleadoId,
        @Schema(description = "Nombre del empleado") String empleadoNombre,
        @Schema(description = "ID del establecimiento") Long establecimientoId,
        @Schema(description = "Nombre del establecimiento") String establecimientoNombre,
        @Schema(description = "Dirección del establecimiento") String establecimientoDireccion,
        @Schema(description = "Teléfono del establecimiento") String establecimientoTelefono,
        @Schema(description = "Estado actual del ticket") EstadoTicket estado,
        @Schema(description = "Precio base (sin descuento ni IVA)") BigDecimal precioBase,
        @Schema(description = "Descuento global aplicado (%)") BigDecimal descuentoTotal,
        @Schema(description = "Si se aplica IVA") boolean aplicarIva,
        @Schema(description = "Porcentaje de IVA") BigDecimal porcentajeIva,
        @Schema(description = "Precio total final (con descuento e IVA si aplica)") BigDecimal precioTotal,
        @Schema(description = "Fecha de apertura del ticket") LocalDate fechaEntrada,
        @Schema(description = "Fecha prevista de entrega") LocalDate fechaPrevista,
        @Schema(description = "Fecha real de entrega al cliente") LocalDate fechaEntrega,
        @Schema(description = "Observaciones internas") String observaciones,
        @Schema(description = "Timestamp de creación") OffsetDateTime createdAt,
        @Schema(description = "Timestamp de última modificación") OffsetDateTime updatedAt,
        @Schema(description = "Líneas de reparación de calzado") List<LineaResponse> lineasCalzado,
        @Schema(description = "Líneas de costura") List<LineaResponse> lineasCostura,
        @Schema(description = "Líneas de duplicado de llave") List<LineaResponse> lineasLlave,
        @Schema(description = "Fotos adjuntas al ticket") List<FotoResponse> fotos
) {}
