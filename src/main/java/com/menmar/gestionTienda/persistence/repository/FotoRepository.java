package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    List<Foto> findByTicketId(Long ticketId);
}
