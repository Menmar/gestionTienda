package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_llave_linea")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TicketLlaveLinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_llave_id", nullable = false)
    private TipoLlave tipoLlave;

    @Column(nullable = false)
    @Builder.Default
    private short cantidad = 1;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal descuento = BigDecimal.ZERO;
}
