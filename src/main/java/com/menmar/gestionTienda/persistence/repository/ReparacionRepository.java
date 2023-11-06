package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReparacionRepository extends JpaRepository<Reparacion, String> {
}
