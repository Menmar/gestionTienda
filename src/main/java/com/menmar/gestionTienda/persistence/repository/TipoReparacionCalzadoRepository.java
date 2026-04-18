package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.TipoReparacionCalzado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoReparacionCalzadoRepository extends JpaRepository<TipoReparacionCalzado, Long> {
    List<TipoReparacionCalzado> findByActivoTrue();
    List<TipoReparacionCalzado> findByFamiliaIdAndActivoTrue(Long familiaId);
}
