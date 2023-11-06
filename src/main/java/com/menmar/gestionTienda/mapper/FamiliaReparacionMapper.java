package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.FamiliaReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.FamiliaReparacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamiliaReparacionMapper {

    @Mapping(ignore = true, target = "tipoReparacion")
    FamiliaReparacion familiaDTOToFamilia(FamiliaReparacionDTO familiaDTO);

    FamiliaReparacionDTO familiaToFamiliaDTO(FamiliaReparacion familia);
}
