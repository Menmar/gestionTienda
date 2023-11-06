package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Column(name = "idUsuario")
    @Id
    private String idUsuario;
    @Column(name = "contrasenya")
    private String contrasenya;
    @Column(name = "nivel")
    private String nivel;
}
