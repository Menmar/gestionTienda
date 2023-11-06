package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.FamiliaReparacionMapper;
import com.menmar.gestionTienda.model.FamiliaReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.FamiliaReparacion;
import com.menmar.gestionTienda.persistence.repository.FamiliaReparacionRepository;
import com.menmar.gestionTienda.service.FamiliaReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FamiliaReparacionServiceImpl implements FamiliaReparacionService {

    @Autowired
    FamiliaReparacionMapper familiaMapper;

    @Autowired
    FamiliaReparacionRepository familiaRepository;

    @Override
    public FamiliaReparacionDTO creaFamila(FamiliaReparacionDTO familiaReparacionDTO) {
        FamiliaReparacion familia = familiaMapper.familiaDTOToFamilia(familiaReparacionDTO);
        return familiaMapper.familiaToFamiliaDTO(familiaRepository.save(familia));
    }

    @Override
    public FamiliaReparacionDTO actualizaFamila(FamiliaReparacionDTO familiaReparacionDTO) {
        FamiliaReparacion familia = familiaMapper.familiaDTOToFamilia(familiaReparacionDTO);
        return familiaMapper.familiaToFamiliaDTO(familiaRepository.save(familia));
    }

    @Override
    public void borraFamila(FamiliaReparacionDTO familiaReparacionDTO) {
        FamiliaReparacion familia = familiaMapper.familiaDTOToFamilia(familiaReparacionDTO);
        familiaRepository.delete(familia);
    }

    @Override
    public List<FamiliaReparacionDTO> consultaFamila() {
        return null;
    }

    @Override
    public FamiliaReparacionDTO consultaFamila(Long idFamilia) {
        return null;
    }
}
