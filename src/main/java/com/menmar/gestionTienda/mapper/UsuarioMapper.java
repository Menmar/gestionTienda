package com.menmar.gestionTienda.mapper;


import com.menmar.gestionTienda.model.UsuarioDTO;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

  Usuario usuarioDTOToUsuario(UsuarioDTO usuarioDTO);

  UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);

  List<UsuarioDTO> usuariosToUsuarioDTO(List<Usuario> usuarios);
}
