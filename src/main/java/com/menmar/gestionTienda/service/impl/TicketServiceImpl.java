package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.exception.RecursoNoEncontradoException;
import com.menmar.gestionTienda.mapper.TicketMapper;
import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.*;
import com.menmar.gestionTienda.persistence.repository.*;
import com.menmar.gestionTienda.service.NotificacionService;
import com.menmar.gestionTienda.service.TicketService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EstablecimientoRepository establecimientoRepository;
    private final TipoReparacionCalzadoRepository tipoCalzadoRepo;
    private final TipoCosturaRepository tipoCosturaRepo;
    private final TipoLlaveRepository tipoLlaveRepo;
    private final PrecioEstabCalzadoRepository precioCalzadoRepo;
    private final PrecioEstabCosturaRepository precioCosturaRepo;
    private final PrecioEstabLlaveRepository precioLlaveRepo;
    private final NotificacionService notificacionService;
    private final TicketMapper ticketMapper;

    @Override
    @Transactional
    public TicketResponse crear(TicketRequest request, String emailEmpleado) {
        var cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", request.clienteId()));

        var empleado = usuarioRepository.findByEmail(emailEmpleado)
                .orElseThrow(() -> new RecursoNoEncontradoException("Empleado no encontrado: " + emailEmpleado));

        Establecimiento establecimiento = null;
        if (request.establecimientoId() != null) {
            establecimiento = establecimientoRepository.findById(request.establecimientoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Establecimiento", request.establecimientoId()));
        }

        var descuentoTotal = request.descuentoTotal() != null ? request.descuentoTotal() : BigDecimal.ZERO;
        var aplicarIva = request.aplicarIva() != null && request.aplicarIva();
        var porcentajeIva = request.porcentajeIva() != null ? request.porcentajeIva() : new BigDecimal("21.00");

        Ticket ticket = Ticket.builder()
                .numeroTicket(generarNumeroTicket(request.tipo()))
                .tipo(request.tipo())
                .cliente(cliente)
                .empleado(empleado)
                .establecimiento(establecimiento)
                .estado(EstadoTicket.PENDIENTE)
                .fechaEntrada(LocalDate.now())
                .fechaPrevista(request.fechaPrevista())
                .observaciones(request.observaciones())
                .descuentoTotal(descuentoTotal)
                .aplicarIva(aplicarIva)
                .porcentajeIva(porcentajeIva)
                .build();

        agregarLineasCalzado(ticket, request, establecimiento);
        agregarLineasCostura(ticket, request, establecimiento);
        agregarLineasLlave(ticket, request, establecimiento);

        ticket.setPrecioTotal(calcularTotal(ticket));

        return ticketMapper.toResponse(ticketRepository.save(ticket));
    }

    private void agregarLineasCalzado(Ticket ticket, TicketRequest request, Establecimiento est) {
        if (request.lineasCalzado() == null) return;
        request.lineasCalzado().forEach(lr -> {
            TipoReparacionCalzado tipo = tipoCalzadoRepo.findById(lr.tipoReparacionId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("TipoReparacionCalzado", lr.tipoReparacionId()));
            BigDecimal precio = resolverPrecio(lr.precioManual(),
                    est != null ? precioCalzadoRepo.findByEstablecimientoIdAndTipoReparacionId(est.getId(), tipo.getId())
                            .map(PrecioEstabCalzado::getPrecio).orElse(null) : null,
                    tipo.getPrecioBase());
            ticket.getLineasCalzado().add(TicketCalzadoLinea.builder()
                    .ticket(ticket)
                    .tipoReparacion(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(precio)
                    .descuento(lr.descuento() != null ? lr.descuento() : BigDecimal.ZERO)
                    .descripcion(lr.descripcion())
                    .build());
        });
    }

    private void agregarLineasCostura(Ticket ticket, TicketRequest request, Establecimiento est) {
        if (request.lineasCostura() == null) return;
        request.lineasCostura().forEach(lr -> {
            TipoCostura tipo = tipoCosturaRepo.findById(lr.tipoCosturaId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("TipoCostura", lr.tipoCosturaId()));
            BigDecimal precio = resolverPrecio(lr.precioManual(),
                    est != null ? precioCosturaRepo.findByEstablecimientoIdAndTipoCosturaId(est.getId(), tipo.getId())
                            .map(PrecioEstabCostura::getPrecio).orElse(null) : null,
                    tipo.getPrecioBase());
            ticket.getLineasCostura().add(TicketCosturaLinea.builder()
                    .ticket(ticket)
                    .tipoCostura(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(precio)
                    .descuento(lr.descuento() != null ? lr.descuento() : BigDecimal.ZERO)
                    .descripcion(lr.descripcion())
                    .build());
        });
    }

    private void agregarLineasLlave(Ticket ticket, TicketRequest request, Establecimiento est) {
        if (request.lineasLlave() == null) return;
        request.lineasLlave().forEach(lr -> {
            TipoLlave tipo = tipoLlaveRepo.findById(lr.tipoLlaveId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("TipoLlave", lr.tipoLlaveId()));
            BigDecimal precio = resolverPrecio(lr.precioManual(),
                    est != null ? precioLlaveRepo.findByEstablecimientoIdAndTipoLlaveId(est.getId(), tipo.getId())
                            .map(PrecioEstabLlave::getPrecio).orElse(null) : null,
                    tipo.getPrecioBase());
            ticket.getLineasLlave().add(TicketLlaveLinea.builder()
                    .ticket(ticket)
                    .tipoLlave(tipo)
                    .cantidad(lr.cantidad())
                    .precioUnitario(precio)
                    .descuento(lr.descuento() != null ? lr.descuento() : BigDecimal.ZERO)
                    .build());
        });
    }

    /** Prioridad: precio manual > precio establecimiento > precio base */
    private BigDecimal resolverPrecio(BigDecimal manual, BigDecimal establecimiento, BigDecimal base) {
        if (manual != null) return manual;
        if (establecimiento != null) return establecimiento;
        return base;
    }

    private BigDecimal calcularTotal(Ticket ticket) {
        BigDecimal base = BigDecimal.ZERO;
        for (TicketCalzadoLinea l : ticket.getLineasCalzado())
            base = base.add(lineaConDescuento(l.getPrecioUnitario(), l.getCantidad(), l.getDescuento()));
        for (TicketCosturaLinea l : ticket.getLineasCostura())
            base = base.add(lineaConDescuento(l.getPrecioUnitario(), l.getCantidad(), l.getDescuento()));
        for (TicketLlaveLinea l : ticket.getLineasLlave())
            base = base.add(lineaConDescuento(l.getPrecioUnitario(), l.getCantidad(), l.getDescuento()));

        // Descuento total
        if (ticket.getDescuentoTotal().compareTo(BigDecimal.ZERO) > 0) {
            var factor = BigDecimal.ONE.subtract(ticket.getDescuentoTotal().divide(new BigDecimal("100")));
            base = base.multiply(factor).setScale(2, RoundingMode.HALF_UP);
        }

        // IVA
        if (ticket.isAplicarIva() && ticket.getPorcentajeIva().compareTo(BigDecimal.ZERO) > 0) {
            var factorIva = BigDecimal.ONE.add(ticket.getPorcentajeIva().divide(new BigDecimal("100")));
            base = base.multiply(factorIva).setScale(2, RoundingMode.HALF_UP);
        }

        return base;
    }

    private BigDecimal lineaConDescuento(BigDecimal precio, short cantidad, BigDecimal descuento) {
        var subtotal = precio.multiply(BigDecimal.valueOf(cantidad));
        if (descuento == null || descuento.compareTo(BigDecimal.ZERO) == 0) return subtotal;
        var factor = BigDecimal.ONE.subtract(descuento.divide(new BigDecimal("100")));
        return subtotal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    private String generarNumeroTicket(TipoTicket tipo) {
        String seq = switch (tipo) {
            case CALZADO -> "seq_ticket_calzado";
            case COSTURA -> "seq_ticket_costura";
            case LLAVE   -> "seq_ticket_llave";
        };
        String prefijo = switch (tipo) {
            case CALZADO -> "CAL";
            case COSTURA -> "COS";
            case LLAVE   -> "LLA";
        };
        long next = ticketRepository.nextVal(seq);
        return "%s-%05d".formatted(prefijo, next);
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
                        .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado: " + numero)));
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
        var estadoAnterior = ticket.getEstado();
        if (request.estado() == EstadoTicket.ENTREGADO) {
            ticket.setFechaEntrega(LocalDate.now());
        }
        ticket.setEstado(request.estado());
        var saved = ticketRepository.save(ticket);

        if (estadoAnterior != EstadoTicket.LISTO && request.estado() == EstadoTicket.LISTO) {
            notificacionService.notificarTicketListo(saved);
        }

        return ticketMapper.toResponse(saved);
    }

    private Ticket findOrThrow(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket", id));
    }
}
