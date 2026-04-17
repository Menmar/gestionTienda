package com.menmar.gestionTienda.model.catalogo;

import java.math.BigDecimal;

public record TipoCosturaResponse(Long id, String nombre, BigDecimal precioBase, boolean activo) {}
