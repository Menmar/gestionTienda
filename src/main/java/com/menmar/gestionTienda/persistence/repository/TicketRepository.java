package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {
    Optional<Ticket> findByNumeroTicket(String numeroTicket);
    boolean existsByNumeroTicket(String numeroTicket);

    @Query(value = "SELECT NEXTVAL(:seq)", nativeQuery = true)
    long nextVal(@Param("seq") String sequenceName);

    long countByFechaEntradaBetween(LocalDate desde, LocalDate hasta);
    long countByEstadoAndFechaEntradaBetween(EstadoTicket estado, LocalDate desde, LocalDate hasta);

    @Query("SELECT t FROM Ticket t WHERE t.estado = :estado AND t.fechaEntrada <= :fecha")
    Page<Ticket> findByEstadoAndFechaEntradaBefore(
            @Param("estado") EstadoTicket estado,
            @Param("fecha") LocalDate fecha,
            Pageable pageable);

    long countByEstablecimientoIdAndFechaEntradaBetween(Long establecimientoId, LocalDate desde, LocalDate hasta);
    long countByEstablecimientoIdAndEstadoAndFechaEntradaBetween(Long establecimientoId, EstadoTicket estado, LocalDate desde, LocalDate hasta);

    @Query("SELECT t FROM Ticket t WHERE t.establecimiento.id = :estId AND t.estado = :estado AND t.fechaEntrada <= :fecha")
    Page<Ticket> findByEstablecimientoIdAndEstadoAndFechaEntradaBefore(
            @Param("estId") Long establecimientoId,
            @Param("estado") EstadoTicket estado,
            @Param("fecha") LocalDate fecha,
            Pageable pageable);
}
