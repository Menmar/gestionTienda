package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.FamiliaReparacionDTO;

import java.util.List;

public interface FamiliaReparacionService {
    FamiliaReparacionDTO creaFamila(FamiliaReparacionDTO familiaReparacionDTO);

    FamiliaReparacionDTO actualizaFamila(FamiliaReparacionDTO familiaReparacionDTO);

    void borraFamila(FamiliaReparacionDTO familiaReparacionDTO);

    List<FamiliaReparacionDTO> consultaFamila();

    FamiliaReparacionDTO consultaFamila(Long idFamilia);
}
