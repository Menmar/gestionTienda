package com.menmar.gestionTienda.model;

import com.menmar.gestionTienda.persistence.entity.Reparacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FotoDTO {

  private String codReparacion;
  private String rutaFoto;
  private Reparacion reparacion;
}
