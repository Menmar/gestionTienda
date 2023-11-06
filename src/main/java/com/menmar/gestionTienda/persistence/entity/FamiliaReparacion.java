package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "familiaReparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FamiliaReparacion {

    @Column(name = "idFamilia")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFamilia;

    @Column(name = "nombreFamilia")
    private String nombreFamilia;

    @OneToMany(mappedBy = "familia")
    private List<TipoReparacion> tipoReparacion;
}
