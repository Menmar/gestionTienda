package com.menmar.gestionTienda.model.ticket;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LineaLlaveRequest(
        @NotNull Long tipoLlaveId,
        @Min(1) short cantidad
) {}
