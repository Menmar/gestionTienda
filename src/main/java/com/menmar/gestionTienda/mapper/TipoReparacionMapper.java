package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.TipoReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.TipoReparacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TipoReparacionMapper {


    @Mapping(ignore = true, target = "familia")
    TipoReparacion tipoDTOToTipo(TipoReparacionDTO tipoDTO);

    TipoReparacionDTO tipoToTipoDTO(TipoReparacion tipo);
}
