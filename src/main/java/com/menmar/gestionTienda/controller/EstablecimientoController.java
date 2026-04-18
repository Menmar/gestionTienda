package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.establecimiento.*;
import com.menmar.gestionTienda.service.EstablecimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/establecimientos")
@RequiredArgsConstructor
@Tag(name = "Establecimientos", description = "Gestión de tiendas, empleados asignados y precios personalizados")
public class EstablecimientoController {

    private final EstablecimientoService service;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear establecimiento")
    public ResponseEntity<EstablecimientoResponse> crear(@Valid @RequestBody EstablecimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    @Operation(summary = "Listar establecimientos activos")
    public ResponseEntity<List<EstablecimientoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener establecimiento por ID")
    public ResponseEntity<EstablecimientoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar establecimiento")
    public ResponseEntity<EstablecimientoResponse> actualizar(@PathVariable Long id,
                                                               @Valid @RequestBody EstablecimientoRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar establecimiento")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        service.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    // ── Empleados ──────────────────────────────────────────────────────

    @PostMapping("/{id}/empleados/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Asignar empleado a establecimiento")
    public ResponseEntity<Void> asignarEmpleado(@PathVariable Long id,
                                                 @PathVariable Long usuarioId) {
        service.asignarEmpleado(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/empleados/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desasignar empleado de establecimiento")
    public ResponseEntity<Void> desasignarEmpleado(@PathVariable Long id,
                                                    @PathVariable Long usuarioId) {
        service.desasignarEmpleado(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    // ── Precios personalizados ─────────────────────────────────────────

    @GetMapping("/{id}/precios/calzado")
    @Operation(summary = "Listar precios personalizados de calzado")
    public ResponseEntity<List<PrecioEstablecimientoResponse>> preciosCalzado(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPreciosCalzado(id));
    }

    @PutMapping("/{id}/precios/calzado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear o actualizar precio personalizado de calzado")
    public ResponseEntity<PrecioEstablecimientoResponse> upsertPrecioCalzado(
            @PathVariable Long id, @Valid @RequestBody PrecioEstablecimientoRequest request) {
        return ResponseEntity.ok(service.upsertPrecioCalzado(id, request));
    }

    @DeleteMapping("/{id}/precios/calzado/{tipoReparacionId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar precio personalizado de calzado (vuelve al precio base)")
    public ResponseEntity<Void> eliminarPrecioCalzado(@PathVariable Long id,
                                                       @PathVariable Long tipoReparacionId) {
        service.eliminarPrecioCalzado(id, tipoReparacionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/precios/costura")
    @Operation(summary = "Listar precios personalizados de costura")
    public ResponseEntity<List<PrecioEstablecimientoResponse>> preciosCostura(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPreciosCostura(id));
    }

    @PutMapping("/{id}/precios/costura")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear o actualizar precio personalizado de costura")
    public ResponseEntity<PrecioEstablecimientoResponse> upsertPrecioCostura(
            @PathVariable Long id, @Valid @RequestBody PrecioEstablecimientoRequest request) {
        return ResponseEntity.ok(service.upsertPrecioCostura(id, request));
    }

    @DeleteMapping("/{id}/precios/costura/{tipoCosturaId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar precio personalizado de costura")
    public ResponseEntity<Void> eliminarPrecioCostura(@PathVariable Long id,
                                                       @PathVariable Long tipoCosturaId) {
        service.eliminarPrecioCostura(id, tipoCosturaId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/precios/llaves")
    @Operation(summary = "Listar precios personalizados de llaves")
    public ResponseEntity<List<PrecioEstablecimientoResponse>> preciosLlaves(@PathVariable Long id) {
        return ResponseEntity.ok(service.listarPreciosLlave(id));
    }

    @PutMapping("/{id}/precios/llaves")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear o actualizar precio personalizado de llaves")
    public ResponseEntity<PrecioEstablecimientoResponse> upsertPrecioLlave(
            @PathVariable Long id, @Valid @RequestBody PrecioEstablecimientoRequest request) {
        return ResponseEntity.ok(service.upsertPrecioLlave(id, request));
    }

    @DeleteMapping("/{id}/precios/llaves/{tipoLlaveId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar precio personalizado de llaves")
    public ResponseEntity<Void> eliminarPrecioLlave(@PathVariable Long id,
                                                     @PathVariable Long tipoLlaveId) {
        service.eliminarPrecioLlave(id, tipoLlaveId);
        return ResponseEntity.noContent().build();
    }

    // ── Precios sugeridos (para el frontend al crear ticket) ───────────

    @GetMapping("/{id}/precios-sugeridos/calzado")
    @Operation(summary = "Precios sugeridos de calzado para un establecimiento",
               description = "Devuelve precio base y precio personalizado del establecimiento (si existe) para cada tipo de reparación activo")
    public ResponseEntity<List<PreciosSugeridosResponse>> sugeridosCalzado(
            @Parameter(description = "ID del establecimiento") @PathVariable Long id) {
        return ResponseEntity.ok(service.preciosSugeridosCalzado(id));
    }

    @GetMapping("/{id}/precios-sugeridos/costura")
    @Operation(summary = "Precios sugeridos de costura para un establecimiento")
    public ResponseEntity<List<PreciosSugeridosResponse>> sugeridosCostura(@PathVariable Long id) {
        return ResponseEntity.ok(service.preciosSugeridosCostura(id));
    }

    @GetMapping("/{id}/precios-sugeridos/llaves")
    @Operation(summary = "Precios sugeridos de llaves para un establecimiento")
    public ResponseEntity<List<PreciosSugeridosResponse>> sugeridosLlaves(@PathVariable Long id) {
        return ResponseEntity.ok(service.preciosSugeridosLlave(id));
    }
}
