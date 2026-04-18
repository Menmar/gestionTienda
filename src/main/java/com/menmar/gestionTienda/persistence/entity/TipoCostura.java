package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipo_costura")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoCostura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;
}
