package com.menmar.gestionTienda.persistence.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

@Getter
@Setter
@Embeddable
public class TokenId implements Serializable {

  @Serial
  private static final long serialVersionUID = 2789394772310175618L;
  @NotNull
  @Column(name = "accessToken", nullable = false, length = Integer.MAX_VALUE)
  private String accessToken;

  @NotNull
  @Column(name = "idUsuario", nullable = false, length = Integer.MAX_VALUE)
  private String idUsuario;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    TokenId entity = (TokenId) o;
    return Objects.equals(this.idUsuario, entity.idUsuario) &&
        Objects.equals(this.accessToken, entity.accessToken);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idUsuario, accessToken);
  }

}