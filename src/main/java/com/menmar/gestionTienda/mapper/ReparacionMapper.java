package com.menmar.gestionTienda.mapper;


import com.menmar.gestionTienda.model.ReparacionDTO;
import com.menmar.gestionTienda.persistence.entity.Reparacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReparacionMapper {
    @Mapping(ignore = true, target = "foto")
    Reparacion reparacionDTOToReparacion(ReparacionDTO reparacionDTO);

    ReparacionDTO reparacionToReparacionDTO(Reparacion reparacion);
}
