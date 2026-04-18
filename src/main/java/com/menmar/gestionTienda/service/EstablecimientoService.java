package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.establecimiento.*;

import java.util.List;

public interface EstablecimientoService {
    EstablecimientoResponse crear(EstablecimientoRequest request);
    List<EstablecimientoResponse> listar();
    EstablecimientoResponse buscarPorId(Long id);
    EstablecimientoResponse actualizar(Long id, EstablecimientoRequest request);
    void desactivar(Long id);
    void asignarEmpleado(Long establecimientoId, Long usuarioId);
    void desasignarEmpleado(Long establecimientoId, Long usuarioId);

    // Precios personalizados
    List<PrecioEstablecimientoResponse> listarPreciosCalzado(Long establecimientoId);
    List<PrecioEstablecimientoResponse> listarPreciosCostura(Long establecimientoId);
    List<PrecioEstablecimientoResponse> listarPreciosLlave(Long establecimientoId);
    PrecioEstablecimientoResponse upsertPrecioCalzado(Long establecimientoId, PrecioEstablecimientoRequest request);
    PrecioEstablecimientoResponse upsertPrecioCostura(Long establecimientoId, PrecioEstablecimientoRequest request);
    PrecioEstablecimientoResponse upsertPrecioLlave(Long establecimientoId, PrecioEstablecimientoRequest request);
    void eliminarPrecioCalzado(Long establecimientoId, Long tipoReparacionId);
    void eliminarPrecioCostura(Long establecimientoId, Long tipoCosturaId);
    void eliminarPrecioLlave(Long establecimientoId, Long tipoLlaveId);

    // Precios sugeridos para el frontend al crear ticket
    List<PreciosSugeridosResponse> preciosSugeridosCalzado(Long establecimientoId);
    List<PreciosSugeridosResponse> preciosSugeridosCostura(Long establecimientoId);
    List<PreciosSugeridosResponse> preciosSugeridosLlave(Long establecimientoId);
}
