package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.proxy.HibernateProxy;

@Entity
@Table(name = "familiaReparacion")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class FamiliaReparacion {

  @Column(name = "idFamilia")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idFamilia;

  @Column(name = "nombreFamilia")
  private String nombreFamilia;

  @OneToMany(mappedBy = "familia")
  @Exclude
  private List<TipoReparacion> tipoReparacion;

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
    FamiliaReparacion that = (FamiliaReparacion) o;
    return getIdFamilia() != null && Objects.equals(getIdFamilia(), that.getIdFamilia());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
