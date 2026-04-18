package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tipo_reparacion_calzado")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TipoReparacionCalzado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "familia_id", nullable = false)
    private FamiliaReparacion familia;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;
}
