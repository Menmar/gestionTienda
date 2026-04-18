package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Establecimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstablecimientoRepository extends JpaRepository<Establecimiento, Long> {
    List<Establecimiento> findByActivoTrue();
    boolean existsByNombreIgnoreCase(String nombre);
}
