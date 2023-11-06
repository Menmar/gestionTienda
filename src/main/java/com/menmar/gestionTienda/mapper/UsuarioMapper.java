package com.menmar.gestionTienda.mapper;


import com.menmar.gestionTienda.model.UsuarioDTO;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    Usuario usuarioDTOToUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);
}
