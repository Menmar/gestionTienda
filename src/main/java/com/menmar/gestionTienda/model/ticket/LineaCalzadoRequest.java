package com.menmar.gestionTienda.model.ticket;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LineaCalzadoRequest(
        @NotNull Long tipoReparacionId,
        @Min(1) short cantidad,
        String descripcion
) {}
