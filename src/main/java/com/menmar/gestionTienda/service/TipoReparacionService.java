package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.TipoReparacionDTO;

import java.util.List;

public interface TipoReparacionService {
    TipoReparacionDTO creaTipo(TipoReparacionDTO tipoReparacionDTO);

    TipoReparacionDTO actualizaTipo(TipoReparacionDTO tipoReparacionDTO);

    void borraTipo(TipoReparacionDTO tipoReparacionDTO);

    List<TipoReparacionDTO> consultaTipo();

    TipoReparacionDTO consultaTipo(Long idTipo);
}
