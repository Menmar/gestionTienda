package com.menmar.gestionTienda.model.catalogo;

import java.math.BigDecimal;

public record TipoLlaveResponse(Long id, String nombre, BigDecimal precioBase, boolean activo) {}
