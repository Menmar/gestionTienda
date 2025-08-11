package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.UsuarioMapper;
import com.menmar.gestionTienda.model.UsuarioDTO;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import com.menmar.gestionTienda.persistence.repository.UsuarioRepository;
import com.menmar.gestionTienda.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {


  private final UsuarioRepository usuarioRepository;
  private final UsuarioMapper usuarioMapper;

  public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
    this.usuarioRepository = usuarioRepository;
    this.usuarioMapper = usuarioMapper;
  }

  @Override
  public UsuarioDTO creaUsuario(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
    return usuarioMapper.usuarioToUsuarioDTO(usuarioRepository.save(usuario));
  }

  @Override
  public UsuarioDTO actualizaUsuario(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
    return usuarioMapper.usuarioToUsuarioDTO(usuarioRepository.save(usuario));
  }

  @Override
  public void borraUsuario(UsuarioDTO usuarioDTO) {
    Usuario usuario = usuarioMapper.usuarioDTOToUsuario(usuarioDTO);
    usuarioRepository.delete(usuario);
  }

  @Override
  public List<UsuarioDTO> consultaUsuario() {
    return usuarioMapper.usuariosToUsuarioDTO(usuarioRepository.findAll());
  }

  @Override
  public UsuarioDTO consultaUsuario(String idUsuario) {
    return usuarioMapper.usuarioToUsuarioDTO(usuarioRepository.findUsuarioByIdUsuario(idUsuario));
  }

  @Override
  public String validaUsuario(UsuarioDTO usuarioDTO) {
    var usuarioDB = consultaUsuario(usuarioDTO.getIdUsuario());
    if (usuarioDB.getIdUsuario().equals(usuarioDTO.getIdUsuario()) && usuarioDB.getContrasenya()
        .equals(usuarioDTO.getContrasenya())) {
      return usuarioDB.getNivel();
    } else {
      return "0";
    }
  }


}
