package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tipoReparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoReparacion {

    @Column(name = "idTipo")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTipo;

    @Column(name = "nombreTipo")
    private String nombreTipo;

    @Column(name = "idFamilia")
    private Long idFamilia;

    @ManyToOne(fetch = FetchType.LAZY)
    private FamiliaReparacion familia;

    @OneToMany(mappedBy = "tipoReparacion")
    private List<Reparacion> reparacion;
}
