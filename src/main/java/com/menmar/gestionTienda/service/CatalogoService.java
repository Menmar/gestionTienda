package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.catalogo.*;

import java.util.List;

/**
 * Gestión del catálogo de la tienda: familias de reparación,
 * tipos de reparación de calzado, costuras y llaves.
 * <p>
 * Las operaciones de lectura están disponibles para todos los roles.
 * Las de escritura requieren {@code ADMIN}.
 */
public interface CatalogoService {

    // ---- Familias ----

    /** Devuelve todas las familias de reparación activas. */
    List<FamiliaReparacionResponse> listarFamilias();

    /**
     * Busca una familia por su ID.
     *
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    FamiliaReparacionResponse buscarFamilia(Long id);

    /** Crea una nueva familia de reparación. */
    FamiliaReparacionResponse crearFamilia(FamiliaReparacionRequest request);

    /** Actualiza el nombre de una familia existente. */
    FamiliaReparacionResponse actualizarFamilia(Long id, FamiliaReparacionRequest request);

    /** Desactiva (soft delete) una familia. */
    void desactivarFamilia(Long id);

    // ---- Tipos reparación calzado ----

    /**
     * Lista tipos de reparación de calzado.
     * Si {@code familiaId} no es nulo, filtra por familia.
     */
    List<TipoReparacionCalzadoResponse> listarTiposCalzado(Long familiaId);

    /** @throws jakarta.persistence.EntityNotFoundException si no existe */
    TipoReparacionCalzadoResponse buscarTipoCalzado(Long id);

    TipoReparacionCalzadoResponse crearTipoCalzado(TipoReparacionCalzadoRequest request);

    TipoReparacionCalzadoResponse actualizarTipoCalzado(Long id, TipoReparacionCalzadoRequest request);

    void desactivarTipoCalzado(Long id);

    // ---- Tipos costura ----

    List<TipoCosturaResponse> listarTiposCostura();

    /** @throws jakarta.persistence.EntityNotFoundException si no existe */
    TipoCosturaResponse buscarTipoCostura(Long id);

    TipoCosturaResponse crearTipoCostura(TipoCosturaRequest request);

    TipoCosturaResponse actualizarTipoCostura(Long id, TipoCosturaRequest request);

    void desactivarTipoCostura(Long id);

    // ---- Tipos llave ----

    List<TipoLlaveResponse> listarTiposLlave();

    /** @throws jakarta.persistence.EntityNotFoundException si no existe */
    TipoLlaveResponse buscarTipoLlave(Long id);

    TipoLlaveResponse crearTipoLlave(TipoLlaveRequest request);

    TipoLlaveResponse actualizarTipoLlave(Long id, TipoLlaveRequest request);

    void desactivarTipoLlave(Long id);
}
