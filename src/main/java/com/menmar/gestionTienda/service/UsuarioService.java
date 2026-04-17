package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.usuario.UsuarioRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioResponse;

import java.util.List;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioRequest request);
    List<UsuarioResponse> listar();
    UsuarioResponse buscarPorId(Long id);
    UsuarioResponse actualizar(Long id, UsuarioRequest request);
    void desactivar(Long id);
}
