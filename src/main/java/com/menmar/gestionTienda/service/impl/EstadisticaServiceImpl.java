package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.estadistica.ResumenEstadisticaResponse;
import com.menmar.gestionTienda.model.estadistica.TicketPendienteRecogidaResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.repository.TicketRepository;
import com.menmar.gestionTienda.service.EstadisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EstadisticaServiceImpl implements EstadisticaService {

    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true)
    public ResumenEstadisticaResponse resumen(LocalDate desde, LocalDate hasta, Long establecimientoId) {
        long total, pendientes, enProceso, listos, entregados, cancelados;
        if (establecimientoId != null) {
            total       = ticketRepository.countByEstablecimientoIdAndFechaEntradaBetween(establecimientoId, desde, hasta);
            pendientes  = ticketRepository.countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(establecimientoId, EstadoTicket.PENDIENTE,  desde, hasta);
            enProceso   = ticketRepository.countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(establecimientoId, EstadoTicket.EN_PROCESO, desde, hasta);
            listos      = ticketRepository.countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(establecimientoId, EstadoTicket.LISTO,      desde, hasta);
            entregados  = ticketRepository.countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(establecimientoId, EstadoTicket.ENTREGADO,  desde, hasta);
            cancelados  = ticketRepository.countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(establecimientoId, EstadoTicket.CANCELADO,  desde, hasta);
        } else {
            total       = ticketRepository.countByFechaEntradaBetween(desde, hasta);
            pendientes  = ticketRepository.countByEstadoAndFechaEntradaBetween(EstadoTicket.PENDIENTE,  desde, hasta);
            enProceso   = ticketRepository.countByEstadoAndFechaEntradaBetween(EstadoTicket.EN_PROCESO, desde, hasta);
            listos      = ticketRepository.countByEstadoAndFechaEntradaBetween(EstadoTicket.LISTO,      desde, hasta);
            entregados  = ticketRepository.countByEstadoAndFechaEntradaBetween(EstadoTicket.ENTREGADO,  desde, hasta);
            cancelados  = ticketRepository.countByEstadoAndFechaEntradaBetween(EstadoTicket.CANCELADO,  desde, hasta);
        }
        return new ResumenEstadisticaResponse(total, pendientes, enProceso, listos, entregados, cancelados);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TicketPendienteRecogidaResponse> pendientesRecogida(int diasDesde, Long establecimientoId, Pageable pageable) {
        var fechaLimite = LocalDate.now().minusDays(diasDesde);
        var page = establecimientoId != null
                ? ticketRepository.findByEstablecimientoIdAndEstadoAndFechaEntradaBefore(establecimientoId, EstadoTicket.LISTO, fechaLimite, pageable)
                : ticketRepository.findByEstadoAndFechaEntradaBefore(EstadoTicket.LISTO, fechaLimite, pageable);

        return PageResponse.of(page.map(t -> new TicketPendienteRecogidaResponse(
                t.getId(),
                t.getNumeroTicket(),
                t.getCliente().getNombre() + " " + (t.getCliente().getApellidos() != null ? t.getCliente().getApellidos() : ""),
                t.getCliente().getTelefono(),
                t.getEstablecimiento() != null ? t.getEstablecimiento().getNombre() : null,
                t.getFechaEntrada(),
                t.getFechaPrevista(),
                ChronoUnit.DAYS.between(t.getFechaEntrada(), LocalDate.now())
        )));
    }
}
