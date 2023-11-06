package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.TipoReparacionMapper;
import com.menmar.gestionTienda.model.TipoReparacionDTO;
import com.menmar.gestionTienda.persistence.repository.TipoReparacionRepository;
import com.menmar.gestionTienda.service.TipoReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoReparacionServiceImpl implements TipoReparacionService {

    @Autowired
    TipoReparacionMapper tipoMapper;

    @Autowired
    TipoReparacionRepository tipoRepository;

    @Override
    public TipoReparacionDTO creaTipo(TipoReparacionDTO tipoReparacionDTO) {
        return null;
    }

    @Override
    public TipoReparacionDTO actualizaTipo(TipoReparacionDTO tipoReparacionDTO) {
        return null;
    }

    @Override
    public void borraTipo(TipoReparacionDTO tipoReparacionDTO) {

    }

    @Override
    public List<TipoReparacionDTO> consultaTipo() {
        return null;
    }

    @Override
    public TipoReparacionDTO consultaTipo(Long idTipo) {
        return null;
    }
}
