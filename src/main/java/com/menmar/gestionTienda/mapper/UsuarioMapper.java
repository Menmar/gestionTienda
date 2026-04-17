package com.menmar.gestionTienda.mapper;

import com.menmar.gestionTienda.model.usuario.UsuarioResponse;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    UsuarioResponse toResponse(Usuario usuario);
}
