package com.menmar.gestionTienda.model;

import com.menmar.gestionTienda.persistence.entity.TipoReparacion;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamiliaReparacionDTO {

  private Long idFamilia;
  private String nombreFamilia;
  private List<TipoReparacion> tipoReparacion;
}
