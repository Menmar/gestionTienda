package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.exception.ConflictoException;
import com.menmar.gestionTienda.exception.RecursoNoEncontradoException;
import com.menmar.gestionTienda.mapper.EstablecimientoMapper;
import com.menmar.gestionTienda.model.establecimiento.*;
import com.menmar.gestionTienda.persistence.entity.*;
import com.menmar.gestionTienda.persistence.repository.*;
import com.menmar.gestionTienda.service.EstablecimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstablecimientoServiceImpl implements EstablecimientoService {

    private final EstablecimientoRepository establecimientoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoReparacionCalzadoRepository tipoCalzadoRepo;
    private final TipoCosturaRepository tipoCosturaRepo;
    private final TipoLlaveRepository tipoLlaveRepo;
    private final PrecioEstabCalzadoRepository precioCalzadoRepo;
    private final PrecioEstabCosturaRepository precioCosturaRepo;
    private final PrecioEstabLlaveRepository precioLlaveRepo;
    private final EstablecimientoMapper mapper;

    @Override
    @Transactional
    public EstablecimientoResponse crear(EstablecimientoRequest request) {
        if (establecimientoRepository.existsByNombreIgnoreCase(request.nombre())) {
            throw new ConflictoException("Ya existe un establecimiento con ese nombre: " + request.nombre());
        }
        return mapper.toResponse(establecimientoRepository.save(mapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EstablecimientoResponse> listar() {
        return establecimientoRepository.findByActivoTrue().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public EstablecimientoResponse buscarPorId(Long id) {
        return mapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public EstablecimientoResponse actualizar(Long id, EstablecimientoRequest request) {
        var est = findOrThrow(id);
        mapper.updateFromRequest(request, est);
        return mapper.toResponse(establecimientoRepository.save(est));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        var est = findOrThrow(id);
        est.setActivo(false);
        establecimientoRepository.save(est);
    }

    @Override
    @Transactional
    public void asignarEmpleado(Long establecimientoId, Long usuarioId) {
        var est = findOrThrow(establecimientoId);
        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", usuarioId));
        usuario.getEstablecimientos().add(est);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void desasignarEmpleado(Long establecimientoId, Long usuarioId) {
        var est = findOrThrow(establecimientoId);
        var usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario", usuarioId));
        usuario.getEstablecimientos().remove(est);
        usuarioRepository.save(usuario);
    }

    // ── Precios calzado ────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PrecioEstablecimientoResponse> listarPreciosCalzado(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return precioCalzadoRepo.findByEstablecimientoId(establecimientoId).stream()
                .map(p -> new PrecioEstablecimientoResponse(
                        p.getId(), establecimientoId,
                        p.getTipoReparacion().getId(), p.getTipoReparacion().getNombre(),
                        p.getTipoReparacion().getPrecioBase(), p.getPrecio()))
                .toList();
    }

    @Override
    @Transactional
    public PrecioEstablecimientoResponse upsertPrecioCalzado(Long establecimientoId, PrecioEstablecimientoRequest request) {
        var est = findOrThrow(establecimientoId);
        var tipo = tipoCalzadoRepo.findById(request.itemId())
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoReparacionCalzado", request.itemId()));
        var precio = precioCalzadoRepo
                .findByEstablecimientoIdAndTipoReparacionId(establecimientoId, request.itemId())
                .orElseGet(() -> PrecioEstabCalzado.builder().establecimiento(est).tipoReparacion(tipo).build());
        precio.setPrecio(request.precio());
        var saved = precioCalzadoRepo.save(precio);
        return new PrecioEstablecimientoResponse(saved.getId(), establecimientoId,
                tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), saved.getPrecio());
    }

    @Override
    @Transactional
    public void eliminarPrecioCalzado(Long establecimientoId, Long tipoReparacionId) {
        findOrThrow(establecimientoId);
        precioCalzadoRepo.deleteByEstablecimientoIdAndTipoReparacionId(establecimientoId, tipoReparacionId);
    }

    // ── Precios costura ────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PrecioEstablecimientoResponse> listarPreciosCostura(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return precioCosturaRepo.findByEstablecimientoId(establecimientoId).stream()
                .map(p -> new PrecioEstablecimientoResponse(
                        p.getId(), establecimientoId,
                        p.getTipoCostura().getId(), p.getTipoCostura().getNombre(),
                        p.getTipoCostura().getPrecioBase(), p.getPrecio()))
                .toList();
    }

    @Override
    @Transactional
    public PrecioEstablecimientoResponse upsertPrecioCostura(Long establecimientoId, PrecioEstablecimientoRequest request) {
        var est = findOrThrow(establecimientoId);
        var tipo = tipoCosturaRepo.findById(request.itemId())
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoCostura", request.itemId()));
        var precio = precioCosturaRepo
                .findByEstablecimientoIdAndTipoCosturaId(establecimientoId, request.itemId())
                .orElseGet(() -> PrecioEstabCostura.builder().establecimiento(est).tipoCostura(tipo).build());
        precio.setPrecio(request.precio());
        var saved = precioCosturaRepo.save(precio);
        return new PrecioEstablecimientoResponse(saved.getId(), establecimientoId,
                tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), saved.getPrecio());
    }

    @Override
    @Transactional
    public void eliminarPrecioCostura(Long establecimientoId, Long tipoCosturaId) {
        findOrThrow(establecimientoId);
        precioCosturaRepo.deleteByEstablecimientoIdAndTipoCosturaId(establecimientoId, tipoCosturaId);
    }

    // ── Precios llave ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PrecioEstablecimientoResponse> listarPreciosLlave(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return precioLlaveRepo.findByEstablecimientoId(establecimientoId).stream()
                .map(p -> new PrecioEstablecimientoResponse(
                        p.getId(), establecimientoId,
                        p.getTipoLlave().getId(), p.getTipoLlave().getNombre(),
                        p.getTipoLlave().getPrecioBase(), p.getPrecio()))
                .toList();
    }

    @Override
    @Transactional
    public PrecioEstablecimientoResponse upsertPrecioLlave(Long establecimientoId, PrecioEstablecimientoRequest request) {
        var est = findOrThrow(establecimientoId);
        var tipo = tipoLlaveRepo.findById(request.itemId())
                .orElseThrow(() -> new RecursoNoEncontradoException("TipoLlave", request.itemId()));
        var precio = precioLlaveRepo
                .findByEstablecimientoIdAndTipoLlaveId(establecimientoId, request.itemId())
                .orElseGet(() -> PrecioEstabLlave.builder().establecimiento(est).tipoLlave(tipo).build());
        precio.setPrecio(request.precio());
        var saved = precioLlaveRepo.save(precio);
        return new PrecioEstablecimientoResponse(saved.getId(), establecimientoId,
                tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), saved.getPrecio());
    }

    @Override
    @Transactional
    public void eliminarPrecioLlave(Long establecimientoId, Long tipoLlaveId) {
        findOrThrow(establecimientoId);
        precioLlaveRepo.deleteByEstablecimientoIdAndTipoLlaveId(establecimientoId, tipoLlaveId);
    }

    // ── Precios sugeridos ──────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<PreciosSugeridosResponse> preciosSugeridosCalzado(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return tipoCalzadoRepo.findByActivoTrue().stream().map(tipo -> {
            var override = precioCalzadoRepo
                    .findByEstablecimientoIdAndTipoReparacionId(establecimientoId, tipo.getId())
                    .map(PrecioEstabCalzado::getPrecio).orElse(null);
            return new PreciosSugeridosResponse(tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), override);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreciosSugeridosResponse> preciosSugeridosCostura(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return tipoCosturaRepo.findByActivoTrue().stream().map(tipo -> {
            var override = precioCosturaRepo
                    .findByEstablecimientoIdAndTipoCosturaId(establecimientoId, tipo.getId())
                    .map(PrecioEstabCostura::getPrecio).orElse(null);
            return new PreciosSugeridosResponse(tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), override);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreciosSugeridosResponse> preciosSugeridosLlave(Long establecimientoId) {
        findOrThrow(establecimientoId);
        return tipoLlaveRepo.findByActivoTrue().stream().map(tipo -> {
            var override = precioLlaveRepo
                    .findByEstablecimientoIdAndTipoLlaveId(establecimientoId, tipo.getId())
                    .map(PrecioEstabLlave::getPrecio).orElse(null);
            return new PreciosSugeridosResponse(tipo.getId(), tipo.getNombre(), tipo.getPrecioBase(), override);
        }).toList();
    }

    private Establecimiento findOrThrow(Long id) {
        return establecimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Establecimiento", id));
    }
}
