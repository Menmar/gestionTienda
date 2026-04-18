package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.PrecioEstabLlave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrecioEstabLlaveRepository extends JpaRepository<PrecioEstabLlave, Long> {
    Optional<PrecioEstabLlave> findByEstablecimientoIdAndTipoLlaveId(Long establecimientoId, Long tipoLlaveId);
    List<PrecioEstabLlave> findByEstablecimientoId(Long establecimientoId);
    void deleteByEstablecimientoIdAndTipoLlaveId(Long establecimientoId, Long tipoLlaveId);
}
