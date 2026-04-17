package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.TicketMapper;
import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.*;
import com.menmar.gestionTienda.persistence.repository.*;
import com.menmar.gestionTienda.service.TicketService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoReparacionCalzadoRepository tipoCalzadoRepo;
    private final TipoCosturaRepository tipoCosturaRepo;
    private final TipoLlaveRepository tipoLlaveRepo;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketResponse crear(TicketRequest request) {
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + request.clienteId()));

        String emailEmpleado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario empleado = usuarioRepository.findByEmail(emailEmpleado)
                .orElseThrow(() -> new NoSuchElementException("Empleado no encontrado: " + emailEmpleado));

        Ticket ticket = Ticket.builder()
                .numeroTicket(generarNumeroTicket(request.tipo()))
                .tipo(request.tipo())
                .cliente(cliente)
                .empleado(empleado)
                .estado(EstadoTicket.PENDIENTE)
                .fechaEntrada(LocalDate.now())
                .fechaPrevista(request.fechaPrevista())
                .observaciones(request.observaciones())
                .build();

        agregarLineasCalzado(ticket, request);
        agregarLineasCostura(ticket, request);
        agregarLineasLlave(ticket, request);

        ticket.setPrecioTotal(calcularTotal(ticket));

        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    private void agregarLineasCalzado(Ticket ticket, TicketRequest request) {
        if (request.lineasCalzado() == null) return;
        request.lineasCalzado().forEach(lr -> {
            TipoReparacionCalzado tipo = tipoCalzadoRepo.findById(lr.tipoReparacionId())
                    .orElseThrow(() -> new NoSuchElementException("Tipo reparación no encontrado: " + lr.tipoReparacionId()));
            ticket.getLineasCalzado().add(TicketCalzadoLinea.builder()
                    .ticket(ticket)
                    .tipoReparacion(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(tipo.getPrecioBase())
                    .descripcion(lr.descripcion())
                    .build());
        });
    }

    private void agregarLineasCostura(Ticket ticket, TicketRequest request) {
        if (request.lineasCostura() == null) return;
        request.lineasCostura().forEach(lr -> {
            TipoCostura tipo = tipoCosturaRepo.findById(lr.tipoCosturaId())
                    .orElseThrow(() -> new NoSuchElementException("Tipo costura no encontrado: " + lr.tipoCosturaId()));
            ticket.getLineasCostura().add(TicketCosturaLinea.builder()
                    .ticket(ticket)
                    .tipoCostura(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(tipo.getPrecioBase())
                    .descripcion(lr.descripcion())
                    .build());
        });
    }

    private void agregarLineasLlave(Ticket ticket, TicketRequest request) {
        if (request.lineasLlave() == null) return;
        request.lineasLlave().forEach(lr -> {
            TipoLlave tipo = tipoLlaveRepo.findById(lr.tipoLlaveId())
                    .orElseThrow(() -> new NoSuchElementException("Tipo llave no encontrado: " + lr.tipoLlaveId()));
            ticket.getLineasLlave().add(TicketLlaveLinea.builder()
                    .ticket(ticket)
                    .tipoLlave(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(tipo.getPrecioBase())
                    .build());
        });
    }

    private BigDecimal calcularTotal(Ticket ticket) {
        BigDecimal total = BigDecimal.ZERO;
        for (TicketCalzadoLinea l : ticket.getLineasCalzado())
            total = total.add(l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad())));
        for (TicketCosturaLinea l : ticket.getLineasCostura())
            total = total.add(l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad())));
        for (TicketLlaveLinea l : ticket.getLineasLlave())
            total = total.add(l.getPrecioUnitario().multiply(BigDecimal.valueOf(l.getCantidad())));
        return total;
    }

    private String generarNumeroTicket(TipoTicket tipo) {
        String prefijo = switch (tipo) {
            case CALZADO -> "CAL";
            case COSTURA -> "COS";
            case LLAVE   -> "LLA";
        };
        long count = ticketRepository.count() + 1;
        return "%s-%05d".formatted(prefijo, count);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse buscarPorId(Long id) {
        return ticketMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse buscarPorNumero(String numero) {
        return ticketMapper.toResponse(
                ticketRepository.findByNumeroTicket(numero)
                        .orElseThrow(() -> new NoSuchElementException("Ticket no encontrado: " + numero)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TicketResponse> listar(EstadoTicket estado, TipoTicket tipo, Long clienteId, Pageable pageable) {
        Specification<Ticket> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (estado != null) predicates.add(cb.equal(root.get("estado"), estado));
            if (tipo != null) predicates.add(cb.equal(root.get("tipo"), tipo));
            if (clienteId != null) predicates.add(cb.equal(root.get("cliente").get("id"), clienteId));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return PageResponse.of(ticketRepository.findAll(spec, pageable).map(ticketMapper::toResponse));
    }

    @Override
    @Transactional
    public TicketResponse cambiarEstado(Long id, CambioEstadoRequest request) {
        Ticket ticket = findOrThrow(id);
        if (request.estado() == EstadoTicket.ENTREGADO) {
            ticket.setFechaEntrega(LocalDate.now());
        }
        ticket.setEstado(request.estado());
        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    private Ticket findOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ticket no encontrado: " + id));
    }
}
