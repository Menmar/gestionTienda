package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.catalogo.*;
import com.menmar.gestionTienda.persistence.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CatalogoMapper {

    FamiliaReparacionResponse toResponse(FamiliaReparacion familia);

    TipoCosturaResponse toResponse(TipoCostura tipoCostura);

    TipoLlaveResponse toResponse(TipoLlave tipoLlave);

    @Mapping(target = "familiaId", source = "familia.id")
    @Mapping(target = "familiaNombre", source = "familia.nombre")
    TipoReparacionCalzadoResponse toResponse(TipoReparacionCalzado tipo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    FamiliaReparacion toEntity(FamiliaReparacionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    @Mapping(target = "familia", ignore = true)
    TipoReparacionCalzado toEntity(TipoReparacionCalzadoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    TipoCostura toEntity(TipoCosturaRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", constant = "true")
    TipoLlave toEntity(TipoLlaveRequest request);

    void updateEntity(FamiliaReparacionRequest request, @MappingTarget FamiliaReparacion familia);
}
