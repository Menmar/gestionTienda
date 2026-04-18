package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.TipoCostura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipoCosturaRepository extends JpaRepository<TipoCostura, Long> {
    List<TipoCostura> findByActivoTrue();
}
