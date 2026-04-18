package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "precio_estab_llave")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PrecioEstabLlave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "establecimiento_id", nullable = false)
    private Establecimiento establecimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_llave_id", nullable = false)
    private TipoLlave tipoLlave;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;
}
