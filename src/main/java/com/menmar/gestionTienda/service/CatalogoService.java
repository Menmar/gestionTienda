package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.catalogo.*;

import java.util.List;

public interface CatalogoService {

    // Familias
    List<FamiliaReparacionResponse> listarFamilias();
    FamiliaReparacionResponse buscarFamilia(Long id);
    FamiliaReparacionResponse crearFamilia(FamiliaReparacionRequest request);
    FamiliaReparacionResponse actualizarFamilia(Long id, FamiliaReparacionRequest request);
    void desactivarFamilia(Long id);

    // Tipos reparación calzado
    List<TipoReparacionCalzadoResponse> listarTiposCalzado(Long familiaId);
    TipoReparacionCalzadoResponse buscarTipoCalzado(Long id);
    TipoReparacionCalzadoResponse crearTipoCalzado(TipoReparacionCalzadoRequest request);
    TipoReparacionCalzadoResponse actualizarTipoCalzado(Long id, TipoReparacionCalzadoRequest request);
    void desactivarTipoCalzado(Long id);

    // Tipos costura
    List<TipoCosturaResponse> listarTiposCostura();
    TipoCosturaResponse buscarTipoCostura(Long id);
    TipoCosturaResponse crearTipoCostura(TipoCosturaRequest request);
    TipoCosturaResponse actualizarTipoCostura(Long id, TipoCosturaRequest request);
    void desactivarTipoCostura(Long id);

    // Tipos llave
    List<TipoLlaveResponse> listarTiposLlave();
    TipoLlaveResponse buscarTipoLlave(Long id);
    TipoLlaveResponse crearTipoLlave(TipoLlaveRequest request);
    TipoLlaveResponse actualizarTipoLlave(Long id, TipoLlaveRequest request);
    void desactivarTipoLlave(Long id);
}
