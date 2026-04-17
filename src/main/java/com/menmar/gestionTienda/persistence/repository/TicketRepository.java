package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByNumeroTicket(String numeroTicket);
    List<Ticket> findByClienteId(Long clienteId);
    List<Ticket> findByEstado(EstadoTicket estado);
    List<Ticket> findByTipo(TipoTicket tipo);
    boolean existsByNumeroTicket(String numeroTicket);
}
