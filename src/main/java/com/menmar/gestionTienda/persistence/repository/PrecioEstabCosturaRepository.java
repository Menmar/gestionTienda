package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.PrecioEstabCostura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrecioEstabCosturaRepository extends JpaRepository<PrecioEstabCostura, Long> {
    Optional<PrecioEstabCostura> findByEstablecimientoIdAndTipoCosturaId(Long establecimientoId, Long tipoCosturaId);
    List<PrecioEstabCostura> findByEstablecimientoId(Long establecimientoId);
    void deleteByEstablecimientoIdAndTipoCosturaId(Long establecimientoId, Long tipoCosturaId);
}
