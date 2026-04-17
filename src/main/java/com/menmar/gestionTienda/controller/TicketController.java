package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import com.menmar.gestionTienda.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> crear(@Valid @RequestBody TicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> listar(
            @RequestParam(required = false) EstadoTicket estado,
            @RequestParam(required = false) TipoTicket tipo,
            @RequestParam(required = false) Long clienteId) {
        if (estado != null) return ResponseEntity.ok(ticketService.listarPorEstado(estado));
        if (tipo != null)   return ResponseEntity.ok(ticketService.listarPorTipo(tipo));
        if (clienteId != null) return ResponseEntity.ok(ticketService.listarPorCliente(clienteId));
        return ResponseEntity.ok(ticketService.listar());
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
}
