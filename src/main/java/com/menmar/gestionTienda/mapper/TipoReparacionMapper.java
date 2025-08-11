package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.TipoReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.TipoReparacion;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoReparacionMapper {


  @Mapping(ignore = true, target = "familia")
  @Mapping(ignore = true, target = "reparacion")
  TipoReparacion tipoDTOToTipo(TipoReparacionDTO tipoDTO);

  TipoReparacionDTO tipoToTipoDTO(TipoReparacion tipo);

  @Mapping(ignore = true, target = "familia")
  @Mapping(ignore = true, target = "reparacion")
  List<TipoReparacion> tipoDTOToTipo(List<TipoReparacionDTO> tipoDTO);

  List<TipoReparacionDTO> tipoToTipoDTO(List<TipoReparacion> tipo);
}
