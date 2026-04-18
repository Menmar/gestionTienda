package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByNumeroTicket(String numeroTicket);
    boolean existsByNumeroTicket(String numeroTicket);
}
