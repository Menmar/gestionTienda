package com.menmar.gestionTienda.model;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Value;

/**
 * DTO for {@link com.menmar.gestionTienda.persistence.entity.Token}
 */
@Value
public class TokenDTO implements Serializable {

  TokenIdDTO id;
  @NotNull
  String refreshToken;
  @NotNull
  LocalDate accessTokenExpirationTime;
  @NotNull
  LocalDate refreshTokenExpirationTime;
}