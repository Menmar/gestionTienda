package com.menmar.gestionTienda.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioDTO {

  private String idUsuario;
  private String contrasenya;
  private String nivel;
}
