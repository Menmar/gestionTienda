package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.ReparacionMapper;
import com.menmar.gestionTienda.model.ReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.Reparacion;
import com.menmar.gestionTienda.persistence.repository.ReparacionRepository;
import com.menmar.gestionTienda.service.ReparacionService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReparacionServiceImpl implements ReparacionService {

  @Autowired
  ReparacionRepository reparacionRepository;
  @Autowired
  ReparacionMapper reparacionMapper;

  @Override
  public ReparacionDTO creaReparacion(ReparacionDTO reparacionDTO) {
    Reparacion reparacion = reparacionMapper.reparacionDTOToReparacion(reparacionDTO);
    return reparacionMapper.reparacionToReparacionDTO(reparacionRepository.save(reparacion));
  }

  @Override
  public ReparacionDTO actualizaReparacion(ReparacionDTO reparacionDTO) {
    Reparacion reparacion = reparacionMapper.reparacionDTOToReparacion(reparacionDTO);
    return reparacionMapper.reparacionToReparacionDTO(reparacionRepository.save(reparacion));
  }

  @Override
  public void borraReparacion(ReparacionDTO reparacionDTO) {
    Reparacion reparacion = reparacionMapper.reparacionDTOToReparacion(reparacionDTO);
    reparacionRepository.delete(reparacion);
  }

  @Override
  public List<ReparacionDTO> consultaReparacion() {
    return reparacionMapper.reparacionToReparacionDTO(
        reparacionRepository.findAll());
  }

  @Override
  public ReparacionDTO consultaReparacion(String codReparacion) {
    return reparacionMapper.reparacionToReparacionDTO(
        reparacionRepository.findReparacionByCodReparacion(codReparacion));
  }
}
