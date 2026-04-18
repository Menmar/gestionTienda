package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import com.menmar.gestionTienda.persistence.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    ClienteResponse toResponse(Cliente cliente);
    Cliente toEntity(ClienteRequest request);
    void updateEntity(ClienteRequest request, @MappingTarget Cliente cliente);
}
