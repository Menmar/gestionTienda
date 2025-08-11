package com.menmar.gestionTienda.model;

import com.menmar.gestionTienda.persistence.entity.Foto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReparacionDTO {

  private String codReparacion;
  private String detalleReparacion;
  private TipoReparacionDTO tipoReparacion;
  private LocalDate fechaEntrada;
  private LocalDate fechaArreglo;
  private LocalDate fechaRecogida;
  private String numTelefono;
  private String comentario;
  private Boolean pieIzq;
  private Boolean pieDer;

  private List<Foto> foto;
}
