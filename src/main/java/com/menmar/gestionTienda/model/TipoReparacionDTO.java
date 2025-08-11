package com.menmar.gestionTienda.model;

import com.menmar.gestionTienda.persistence.entity.Reparacion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoReparacionDTO {

  private Long idTipo;
  private String nombreTipo;
  private Long idFamilia;
  private FamiliaReparacionDTO familia;
  private List<Reparacion> reparacion;
}
