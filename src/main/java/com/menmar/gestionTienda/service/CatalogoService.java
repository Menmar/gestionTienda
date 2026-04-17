package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.catalogo.*;

import java.util.List;

public interface CatalogoService {

    // Familias reparacion
    List<FamiliaReparacionResponse> listarFamilias();
    FamiliaReparacionResponse crearFamilia(FamiliaReparacionRequest request);
    FamiliaReparacionResponse actualizarFamilia(Long id, FamiliaReparacionRequest request);
    void desactivarFamilia(Long id);

    // Tipos reparacion calzado
    List<TipoReparacionCalzadoResponse> listarTiposCalzado(Long familiaId);
    TipoReparacionCalzadoResponse crearTipoCalzado(TipoReparacionCalzadoRequest request);
    TipoReparacionCalzadoResponse actualizarTipoCalzado(Long id, TipoReparacionCalzadoRequest request);
    void desactivarTipoCalzado(Long id);

    // Tipos costura
    List<TipoCosturaResponse> listarTiposCostura();
    TipoCosturaResponse crearTipoCostura(TipoCosturaRequest request);
    TipoCosturaResponse actualizarTipoCostura(Long id, TipoCosturaRequest request);
    void desactivarTipoCostura(Long id);

    // Tipos llave
    List<TipoLlaveResponse> listarTiposLlave();
    TipoLlaveResponse crearTipoLlave(TipoLlaveRequest request);
    TipoLlaveResponse actualizarTipoLlave(Long id, TipoLlaveRequest request);
    void desactivarTipoLlave(Long id);
}
