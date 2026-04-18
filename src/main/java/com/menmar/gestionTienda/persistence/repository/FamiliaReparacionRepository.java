package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.FamiliaReparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamiliaReparacionRepository extends JpaRepository<FamiliaReparacion, Long> {
    List<FamiliaReparacion> findByActivoTrue();
}
