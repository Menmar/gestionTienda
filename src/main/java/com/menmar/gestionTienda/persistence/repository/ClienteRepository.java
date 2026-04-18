package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByTelefono(String telefono);
    boolean existsByTelefono(String telefono);
    Page<Cliente> findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombre, String apellidos, Pageable pageable);
}
