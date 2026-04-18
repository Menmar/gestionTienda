package com.menmar.gestionTienda.model.ticket;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Línea de detalle de un ticket (reparación, costura o llave)")
public record LineaResponse(
        @Schema(description = "ID de la línea") Long id,
        @Schema(description = "Nombre del tipo de reparación o llave", example = "Media suela cuero") String tipoNombre,
        @Schema(description = "Unidades", example = "1") short cantidad,
        @Schema(description = "Precio por unidad", example = "12.50") BigDecimal precioUnitario,
        @Schema(description = "Descuento aplicado a la línea (%)", example = "0.00") BigDecimal descuento,
        @Schema(description = "Subtotal con descuento aplicado", example = "12.50") BigDecimal subtotal,
        @Schema(description = "Descripción adicional opcional", example = "Talla 42, color negro") String descripcion
) {}
