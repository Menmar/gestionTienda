package com.menmar.gestionTienda.model;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Value;

/**
 * DTO for {@link com.menmar.gestionTienda.persistence.entity.id.TokenId}
 */
@Value
public class TokenIdDTO implements Serializable {

  @NotNull
  String accesstoken;
  @NotNull
  String idUsuario;
}