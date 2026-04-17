package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.CatalogoMapper;
import com.menmar.gestionTienda.model.catalogo.*;
import com.menmar.gestionTienda.persistence.entity.*;
import com.menmar.gestionTienda.persistence.repository.*;
import com.menmar.gestionTienda.service.CatalogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CatalogoServiceImpl implements CatalogoService {

    private final FamiliaReparacionRepository familiaRepo;
    private final TipoReparacionCalzadoRepository tipoCalzadoRepo;
    private final TipoCosturaRepository tipoCosturaRepo;
    private final TipoLlaveRepository tipoLlaveRepo;
    private final CatalogoMapper mapper;

    // ---- FAMILIAS ----

    @Override
    @Transactional(readOnly = true)
    public List<FamiliaReparacionResponse> listarFamilias() {
        return familiaRepo.findByActivoTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FamiliaReparacionResponse buscarFamilia(Long id) {
        return mapper.toResponse(findFamiliaOrThrow(id));
    }

    @Override
    @Transactional
    public FamiliaReparacionResponse crearFamilia(FamiliaReparacionRequest request) {
        return mapper.toResponse(familiaRepo.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public FamiliaReparacionResponse actualizarFamilia(Long id, FamiliaReparacionRequest request) {
        var familia = findFamiliaOrThrow(id);
        mapper.updateEntity(request, familia);
        return mapper.toResponse(familiaRepo.save(familia));
    }

    @Override
    @Transactional
    public void desactivarFamilia(Long id) {
        var familia = findFamiliaOrThrow(id);
        familia.setActivo(false);
        familiaRepo.save(familia);
    }

    // ---- TIPOS CALZADO ----

    @Override
    @Transactional(readOnly = true)
    public List<TipoReparacionCalzadoResponse> listarTiposCalzado(Long familiaId) {
        var lista = familiaId != null
                ? tipoCalzadoRepo.findByFamiliaIdAndActivoTrue(familiaId)
                : tipoCalzadoRepo.findByActivoTrue();
        return lista.stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoReparacionCalzadoResponse buscarTipoCalzado(Long id) {
        return mapper.toResponse(findCalzadoOrThrow(id));
    }

    @Override
    @Transactional
    public TipoReparacionCalzadoResponse crearTipoCalzado(TipoReparacionCalzadoRequest request) {
        var familia = findFamiliaOrThrow(request.familiaId());
        var tipo = mapper.toEntity(request);
        tipo.setFamilia(familia);
        return mapper.toResponse(tipoCalzadoRepo.save(tipo));
    }

    @Override
    @Transactional
    public TipoReparacionCalzadoResponse actualizarTipoCalzado(Long id, TipoReparacionCalzadoRequest request) {
        var tipo = findCalzadoOrThrow(id);
        var familia = findFamiliaOrThrow(request.familiaId());
        tipo.setNombre(request.nombre());
        tipo.setPrecioBase(request.precioBase());
        tipo.setFamilia(familia);
        return mapper.toResponse(tipoCalzadoRepo.save(tipo));
    }

    @Override
    @Transactional
    public void desactivarTipoCalzado(Long id) {
        var tipo = findCalzadoOrThrow(id);
        tipo.setActivo(false);
        tipoCalzadoRepo.save(tipo);
    }

    // ---- TIPOS COSTURA ----

    @Override
    @Transactional(readOnly = true)
    public List<TipoCosturaResponse> listarTiposCostura() {
        return tipoCosturaRepo.findByActivoTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoCosturaResponse buscarTipoCostura(Long id) {
        return mapper.toResponse(findCosturaOrThrow(id));
    }

    @Override
    @Transactional
    public TipoCosturaResponse crearTipoCostura(TipoCosturaRequest request) {
        return mapper.toResponse(tipoCosturaRepo.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public TipoCosturaResponse actualizarTipoCostura(Long id, TipoCosturaRequest request) {
        var tipo = findCosturaOrThrow(id);
        tipo.setNombre(request.nombre());
        tipo.setPrecioBase(request.precioBase());
        return mapper.toResponse(tipoCosturaRepo.save(tipo));
    }

    @Override
    @Transactional
    public void desactivarTipoCostura(Long id) {
        var tipo = findCosturaOrThrow(id);
        tipo.setActivo(false);
        tipoCosturaRepo.save(tipo);
    }

    // ---- TIPOS LLAVE ----

    @Override
    @Transactional(readOnly = true)
    public List<TipoLlaveResponse> listarTiposLlave() {
        return tipoLlaveRepo.findByActivoTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TipoLlaveResponse buscarTipoLlave(Long id) {
        return mapper.toResponse(findLlaveOrThrow(id));
    }

    @Override
    @Transactional
    public TipoLlaveResponse crearTipoLlave(TipoLlaveRequest request) {
        return mapper.toResponse(tipoLlaveRepo.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional
    public TipoLlaveResponse actualizarTipoLlave(Long id, TipoLlaveRequest request) {
        var tipo = findLlaveOrThrow(id);
        tipo.setNombre(request.nombre());
        tipo.setPrecioBase(request.precioBase());
        return mapper.toResponse(tipoLlaveRepo.save(tipo));
    }

    @Override
    @Transactional
    public void desactivarTipoLlave(Long id) {
        var tipo = findLlaveOrThrow(id);
        tipo.setActivo(false);
        tipoLlaveRepo.save(tipo);
    }

    // ---- helpers ----

    private FamiliaReparacion findFamiliaOrThrow(Long id) {
        return familiaRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Familia no encontrada: " + id));
    }

    private TipoReparacionCalzado findCalzadoOrThrow(Long id) {
        return tipoCalzadoRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo reparación no encontrado: " + id));
    }

    private TipoCostura findCosturaOrThrow(Long id) {
        return tipoCosturaRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo costura no encontrado: " + id));
    }

    private TipoLlave findLlaveOrThrow(Long id) {
        return tipoLlaveRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tipo llave no encontrado: " + id));
    }
}
