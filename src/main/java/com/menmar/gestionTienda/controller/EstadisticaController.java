package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.estadistica.ResumenEstadisticaResponse;
import com.menmar.gestionTienda.model.estadistica.TicketPendienteRecogidaResponse;
import com.menmar.gestionTienda.service.EstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Resúmenes y alertas operativas")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @GetMapping("/resumen")
    @Operation(summary = "Resumen de tickets por período",
               description = "Devuelve contadores por estado para el rango de fechas indicado. " +
                             "Sin establecimientoId devuelve datos de todas las tiendas.")
    public ResponseEntity<ResumenEstadisticaResponse> resumen(
            @Parameter(description = "Fecha inicio (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,

            @Parameter(description = "Fecha fin (YYYY-MM-DD)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,

            @Parameter(description = "ID del establecimiento (opcional)")
            @RequestParam(required = false) Long establecimientoId) {
        return ResponseEntity.ok(estadisticaService.resumen(desde, hasta, establecimientoId));
    }

    @GetMapping("/pendientes-recogida")
    @Operation(summary = "Tickets listos sin recoger",
               description = "Lista tickets en estado LISTO con más de X días desde su apertura. " +
                             "Por defecto 30 días. Útil para reclamar al cliente.")
    public ResponseEntity<PageResponse<TicketPendienteRecogidaResponse>> pendientesRecogida(
            @Parameter(description = "Días mínimos de espera (por defecto 30)")
            @RequestParam(defaultValue = "30") int diasDesde,

            @Parameter(description = "ID del establecimiento (opcional)")
            @RequestParam(required = false) Long establecimientoId,

            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("fechaEntrada").ascending());
        return ResponseEntity.ok(estadisticaService.pendientesRecogida(diasDesde, establecimientoId, pageable));
    }
}
