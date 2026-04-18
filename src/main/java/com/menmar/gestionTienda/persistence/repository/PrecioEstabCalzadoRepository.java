package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.PrecioEstabCalzado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrecioEstabCalzadoRepository extends JpaRepository<PrecioEstabCalzado, Long> {
    Optional<PrecioEstabCalzado> findByEstablecimientoIdAndTipoReparacionId(Long establecimientoId, Long tipoReparacionId);
    List<PrecioEstabCalzado> findByEstablecimientoId(Long establecimientoId);
    void deleteByEstablecimientoIdAndTipoReparacionId(Long establecimientoId, Long tipoReparacionId);
}
