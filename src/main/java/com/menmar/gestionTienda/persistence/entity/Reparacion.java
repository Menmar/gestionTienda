package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reparacion {

    @Column(name = "codReparacion")
    @Id
    private String codReparacion;

    @Column(name = "detalleReparacion")
    private String detalleReparacion;

    @ManyToOne(fetch = FetchType.LAZY)
    private TipoReparacion tipoReparacion;

    @Column(name = "fechaEntrada")
    private LocalDate fechaEntrada;

    @Column(name = "fechaArreglo")
    private LocalDate fechaArreglo;

    @Column(name = "fechaRecogida")
    private LocalDate fechaRecogida;

    @Column(name = "numTelefono")
    private String numTelefono;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "pieIzq")
    private Boolean pieIzq;

    @Column(name = "pieDer")
    private Boolean pieDer;

    @OneToMany(mappedBy = "reparacion")
    private List<Foto> foto;

}
