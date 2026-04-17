package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import com.menmar.gestionTienda.service.PdfService;
import com.menmar.gestionTienda.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;
    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<TicketResponse> crear(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.crear(request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<TicketResponse>> listar(
            @RequestParam(required = false) EstadoTicket estado,
            @RequestParam(required = false) TipoTicket tipo,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ticketService.listar(estado, tipo, clienteId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.buscarPorId(id));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<TicketResponse> buscarPorNumero(@PathVariable String numero) {
        return ResponseEntity.ok(ticketService.buscarPorNumero(numero));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TicketResponse> cambiarEstado(@PathVariable Long id,
                                                        @Valid @RequestBody CambioEstadoRequest request) {
        return ResponseEntity.ok(ticketService.cambiarEstado(id, request));
    }

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        var pdf = pdfService.generarResguardoTicket(id);
        var headers = new HttpHeaders();
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("resguardo-" + id + ".pdf")
                        .build());
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
