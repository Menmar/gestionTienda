package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "familia_reparacion")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FamiliaReparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;
}
