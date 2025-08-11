package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tipoReparacion")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class TipoReparacion {

  @Column(name = "idTipo")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long idTipo;

  @Column(name = "nombreTipo")
  private String nombreTipo;

  @Column(name = "idFamilia")
  private Long idFamilia;

  @ManyToOne(fetch = FetchType.LAZY)
  @Exclude
  private FamiliaReparacion familia;

  @OneToMany(mappedBy = "tipoReparacion")
  @Exclude
  private List<Reparacion> reparacion;

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
    TipoReparacion that = (TipoReparacion) o;
    return getIdTipo() != null && Objects.equals(getIdTipo(), that.getIdTipo());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
