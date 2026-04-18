package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import com.menmar.gestionTienda.service.PdfService;
import com.menmar.gestionTienda.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Operativa diaria: apertura, consulta, cambio de estado y descarga del resguardo PDF")
public class TicketController {

    private final TicketService ticketService;
    private final PdfService pdfService;

    @PostMapping
    @Operation(summary = "Abrir ticket", description = "Crea un nuevo ticket de reparación o duplicado de llaves. El empleado se obtiene del token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ticket creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente o tipo de reparación no encontrado")
    })
    public ResponseEntity<TicketResponse> crear(@Valid @RequestBody TicketRequest request,
                                                @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ticketService.crear(request, principal.getUsername()));
    }

    @GetMapping
    @Operation(summary = "Listar tickets", description = "Devuelve tickets paginados con filtros opcionales por estado, tipo y cliente. Ordenados del más reciente al más antiguo.")
    @ApiResponse(responseCode = "200", description = "Listado de tickets")
    public ResponseEntity<PageResponse<TicketResponse>> listar(
            @Parameter(description = "Filtrar por estado (ABIERTO, EN_PROCESO, LISTO, ENTREGADO, CANCELADO)") @RequestParam(required = false) EstadoTicket estado,
            @Parameter(description = "Filtrar por tipo (CALZADO, COSTURA, LLAVE)") @RequestParam(required = false) TipoTicket tipo,
            @Parameter(description = "Filtrar por ID de cliente") @RequestParam(required = false) Long clienteId,
            @Parameter(description = "Número de página (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página") @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ticketService.listar(estado, tipo, clienteId, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ticket por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<TicketResponse> buscar(
            @Parameter(description = "ID del ticket") @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.buscarPorId(id));
    }

    @GetMapping("/numero/{numero}")
    @Operation(summary = "Buscar ticket por número", description = "Permite localizar un ticket escaneando o tecleando el código impreso en el resguardo.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
            @ApiResponse(responseCode = "404", description = "Número de ticket no encontrado")
    })
    public ResponseEntity<TicketResponse> buscarPorNumero(
            @Parameter(description = "Código alfanumérico del ticket (ej. TK-20240101-0001)") @PathVariable String numero) {
        return ResponseEntity.ok(ticketService.buscarPorNumero(numero));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del ticket", description = "Actualiza el ciclo de vida del ticket: ABIERTO → EN_PROCESO → LISTO → ENTREGADO (o CANCELADO en cualquier momento).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<TicketResponse> cambiarEstado(
            @Parameter(description = "ID del ticket") @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request) {
        return ResponseEntity.ok(ticketService.cambiarEstado(id, request));
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    @Operation(summary = "Descargar resguardo PDF", description = "Genera y devuelve el resguardo A5 del ticket listo para imprimir.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "PDF generado", content = @Content(mediaType = "application/pdf")),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<byte[]> descargarPdf(
            @Parameter(description = "ID del ticket") @PathVariable Long id) {
        var pdf = pdfService.generarResguardoTicket(id);
        var headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("resguardo-" + id + ".pdf")
                        .build());
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
