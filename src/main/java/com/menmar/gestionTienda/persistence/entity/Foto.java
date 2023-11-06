package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "foto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Foto {

    @ManyToOne(fetch = FetchType.LAZY)
    private Reparacion reparacion;

    @Column(name = "rutaFoto")
    @Id
    private String rutaFoto;
}
