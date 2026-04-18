package com.menmar.gestionTienda.model.estadistica;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumen estadístico de tickets para un período y establecimiento")
public record ResumenEstadisticaResponse(
        @Schema(description = "Total de tickets abiertos en el período") long totalAbiertos,
        @Schema(description = "Tickets en estado PENDIENTE") long pendientes,
        @Schema(description = "Tickets en estado EN_PROCESO") long enProceso,
        @Schema(description = "Tickets en estado LISTO (pendientes de recogida)") long listos,
        @Schema(description = "Tickets entregados en el período") long entregados,
        @Schema(description = "Tickets cancelados en el período") long cancelados
) {}
