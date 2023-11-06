package com.menmar.gestionTienda.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoReparacionDTO {

    private Long idTipo;
    private String nombreTipo;
    private Long idFamilia;
    private FamiliaReparacionDTO familia;
}
