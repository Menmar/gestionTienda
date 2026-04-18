package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import com.menmar.gestionTienda.persistence.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    @Mapping(target = "canalesNotificacion", expression = "java(safeCanales(request.canalesNotificacion()))")
    Cliente toEntity(ClienteRequest request);

    @Mapping(target = "canalesNotificacion", expression = "java(safeCanales(request.canalesNotificacion()))")
    void updateEntity(ClienteRequest request, @MappingTarget Cliente cliente);

    default Set<CanalNotificacion> safeCanales(Set<CanalNotificacion> canales) {
        return canales != null ? canales : Collections.emptySet();
    }
}
