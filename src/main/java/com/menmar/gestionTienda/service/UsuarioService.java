package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    UsuarioDTO creaUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO actualizaUsuario(UsuarioDTO usuarioDTO);

    void borraUsuario(UsuarioDTO usuarioDTO);

    List<UsuarioDTO> consultaUsuario();

    UsuarioDTO consultaUsuario(String idUsuario);
}
