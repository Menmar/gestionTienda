package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.usuario.UsuarioRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioResponse;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {
    UsuarioResponse crear(UsuarioRequest request);
    PageResponse<UsuarioResponse> listar(Pageable pageable);
    UsuarioResponse buscarPorId(Long id);
    UsuarioResponse actualizar(Long id, UsuarioRequest request);
    void desactivar(Long id);
}
