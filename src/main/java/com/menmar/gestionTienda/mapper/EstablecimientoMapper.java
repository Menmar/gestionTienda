package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.establecimiento.EstablecimientoRequest;
import com.menmar.gestionTienda.model.establecimiento.EstablecimientoResponse;
import com.menmar.gestionTienda.persistence.entity.Establecimiento;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EstablecimientoMapper {

    Establecimiento toEntity(EstablecimientoRequest request);

    EstablecimientoResponse toResponse(Establecimiento establecimiento);

    void updateFromRequest(EstablecimientoRequest request, @MappingTarget Establecimiento establecimiento);
}
