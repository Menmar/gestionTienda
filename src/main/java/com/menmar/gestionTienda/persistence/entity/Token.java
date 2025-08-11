package com.menmar.gestionTienda.persistence.entity;

import com.menmar.gestionTienda.persistence.entity.id.TokenId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "token")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Token {

  @EmbeddedId
  private TokenId id;

  @NotNull
  @Column(name = "refreshToken", nullable = false, length = Integer.MAX_VALUE)
  private String refreshToken;

  @NotNull
  @Column(name = "accessTokenExpirationTime", nullable = false)
  private LocalDate accessTokenExpirationTime;

  @NotNull
  @Column(name = "refreshTokenExpirationTime", nullable = false)
  private LocalDate refreshTokenExpirationTime;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass = o instanceof HibernateProxy
        ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    Token token = (Token) o;
    return getId() != null && Objects.equals(getId(), token.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(id);
  }
}