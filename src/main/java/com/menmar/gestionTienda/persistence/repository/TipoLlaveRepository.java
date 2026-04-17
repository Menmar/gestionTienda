package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.TipoLlave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoLlaveRepository extends JpaRepository<TipoLlave, Long> {
    List<TipoLlave> findByActivoTrue();
}
