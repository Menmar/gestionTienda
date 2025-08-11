package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.FamiliaReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.FamiliaReparacion;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamiliaReparacionMapper {

  @Mapping(ignore = true, target = "tipoReparacion")
  FamiliaReparacion familiaDTOToFamilia(FamiliaReparacionDTO familiaDTO);

  FamiliaReparacionDTO familiaToFamiliaDTO(FamiliaReparacion familia);

  @Mapping(ignore = true, target = "tipoReparacion")
  List<FamiliaReparacion> familiaDTOToFamilia(List<FamiliaReparacionDTO> familiaDTO);

  List<FamiliaReparacionDTO> familiaToFamiliaDTO(List<FamiliaReparacion> familia);
}
