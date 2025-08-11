package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.TipoReparacionMapper;
import com.menmar.gestionTienda.model.TipoReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.TipoReparacion;
import com.menmar.gestionTienda.persistence.repository.TipoReparacionRepository;
import com.menmar.gestionTienda.service.TipoReparacionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TipoReparacionServiceImpl implements TipoReparacionService {

  @Autowired
  TipoReparacionMapper tipoMapper;

  @Autowired
  TipoReparacionRepository tipoRepository;

  @Override
  public TipoReparacionDTO creaTipo(TipoReparacionDTO tipoReparacionDTO) {
    TipoReparacion tipoReparacion = tipoMapper.tipoDTOToTipo(tipoReparacionDTO);
    return tipoMapper.tipoToTipoDTO(tipoRepository.save(tipoReparacion));
  }

  @Override
  public TipoReparacionDTO actualizaTipo(TipoReparacionDTO tipoReparacionDTO) {
    TipoReparacion tipoReparacion = tipoMapper.tipoDTOToTipo(tipoReparacionDTO);
    return tipoMapper.tipoToTipoDTO(tipoRepository.save(tipoReparacion));
  }

  @Override
  public void borraTipo(TipoReparacionDTO tipoReparacionDTO) {
    TipoReparacion tipoReparacion = tipoMapper.tipoDTOToTipo(tipoReparacionDTO);
    tipoRepository.delete(tipoReparacion);
  }

  @Override
  public List<TipoReparacionDTO> consultaTipo() {
    return tipoMapper.tipoToTipoDTO(
        tipoRepository.findAll());
  }

  @Override
  public TipoReparacionDTO consultaTipo(Long idTipo) {
    return tipoMapper.tipoToTipoDTO(
        tipoRepository.findTipoReparacionByIdTipo(idTipo));
  }
}
