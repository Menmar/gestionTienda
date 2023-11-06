package com.menmar.gestionTienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReparacionDTO {

    private String codReparacion;
    private String detalleReparacion;
    private TipoReparacionDTO tipoReparacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaArreglo;
    private LocalDate fechaRecogida;
    private String numTelefono;
    private String comentario;
    private Boolean pieIzq;
    private Boolean pieDer;

}
