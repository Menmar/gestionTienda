package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.springframework.data.domain.Pageable;

public interface TicketService {
    TicketResponse crear(TicketRequest request, String emailEmpleado);
    TicketResponse buscarPorId(Long id);
    TicketResponse buscarPorNumero(String numero);
    PageResponse<TicketResponse> listar(EstadoTicket estado, TipoTicket tipo, Long clienteId, Pageable pageable);
    TicketResponse cambiarEstado(Long id, CambioEstadoRequest request);
}
