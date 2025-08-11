package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
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
@Table(name = "reparacion")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Reparacion {

  @Column(name = "codReparacion")
  @Id
  private String codReparacion;

  @Column(name = "detalleReparacion")
  private String detalleReparacion;

  @ManyToOne(fetch = FetchType.LAZY)
  @Exclude
  private TipoReparacion tipoReparacion;

  @Column(name = "fechaEntrada")
  private LocalDate fechaEntrada;

  @Column(name = "fechaArreglo")
  private LocalDate fechaArreglo;

  @Column(name = "fechaRecogida")
  private LocalDate fechaRecogida;

  @Column(name = "numTelefono")
  private String numTelefono;

  @Column(name = "comentario")
  private String comentario;

  @Column(name = "pieIzq")
  private Boolean pieIzq;

  @Column(name = "pieDer")
  private Boolean pieDer;

  @OneToMany(mappedBy = "reparacion")
  @Exclude
  private List<Foto> foto;

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
    Reparacion that = (Reparacion) o;
    return getCodReparacion() != null && Objects.equals(getCodReparacion(), that.getCodReparacion());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer()
        .getPersistentClass()
        .hashCode() : getClass().hashCode();
  }
}
