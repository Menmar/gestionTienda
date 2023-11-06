package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.ReparacionDTO;

import java.util.List;

public interface ReparacionService {
    ReparacionDTO creaReparacion(ReparacionDTO reparacionDTO);

    ReparacionDTO actualizaReparacion(ReparacionDTO reparacionDTO);

    void borraReparacion(ReparacionDTO reparacionDTO);

    List<ReparacionDTO> consultaReparacion();

    ReparacionDTO consultaReparacion(String codReparacion);
}
