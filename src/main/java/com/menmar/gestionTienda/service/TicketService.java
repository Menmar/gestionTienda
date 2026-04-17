package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;

import java.util.List;

public interface TicketService {
    TicketResponse crear(TicketRequest request);
    TicketResponse buscarPorId(Long id);
    TicketResponse buscarPorNumero(String numero);
    List<TicketResponse> listar();
    List<TicketResponse> listarPorCliente(Long clienteId);
    List<TicketResponse> listarPorEstado(EstadoTicket estado);
    List<TicketResponse> listarPorTipo(TipoTicket tipo);
    TicketResponse cambiarEstado(Long id, CambioEstadoRequest request);
}
