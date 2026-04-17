package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.catalogo.*;
import com.menmar.gestionTienda.service.CatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalogo")
@RequiredArgsConstructor
@Tag(name = "Catálogo", description = "Gestión del catálogo: familias, reparaciones de calzado, costuras y tipos de llave. Lectura libre; escritura solo ADMIN.")
public class CatalogoController {

    private final CatalogoService catalogoService;

    // ---- FAMILIAS ----

    @GetMapping("/familias")
    @Operation(summary = "Listar familias de reparación")
    @ApiResponse(responseCode = "200", description = "Listado de familias activas")
    public ResponseEntity<List<FamiliaReparacionResponse>> listarFamilias() {
        return ResponseEntity.ok(catalogoService.listarFamilias());
    }

    @GetMapping("/familias/{id}")
    @Operation(summary = "Buscar familia por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Familia encontrada"),
            @ApiResponse(responseCode = "404", description = "Familia no encontrada")
    })
    public ResponseEntity<FamiliaReparacionResponse> buscarFamilia(
            @Parameter(description = "ID de la familia") @PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarFamilia(id));
    }

    @PostMapping("/familias")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear familia de reparación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Familia creada"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<FamiliaReparacionResponse> crearFamilia(@Valid @RequestBody FamiliaReparacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearFamilia(req));
    }

    @PutMapping("/familias/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar familia de reparación")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Familia actualizada"),
            @ApiResponse(responseCode = "404", description = "Familia no encontrada")
    })
    public ResponseEntity<FamiliaReparacionResponse> actualizarFamilia(
            @Parameter(description = "ID de la familia") @PathVariable Long id,
            @Valid @RequestBody FamiliaReparacionRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarFamilia(id, req));
    }

    @DeleteMapping("/familias/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar familia", description = "Soft delete: marca la familia como inactiva.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Familia desactivada"),
            @ApiResponse(responseCode = "404", description = "Familia no encontrada")
    })
    public ResponseEntity<Void> desactivarFamilia(
            @Parameter(description = "ID de la familia") @PathVariable Long id) {
        catalogoService.desactivarFamilia(id);
        return ResponseEntity.noContent().build();
    }

    // ---- TIPOS CALZADO ----

    @GetMapping("/reparaciones-calzado")
    @Operation(summary = "Listar tipos de reparación de calzado", description = "Si se pasa `familiaId` devuelve solo los tipos de esa familia.")
    @ApiResponse(responseCode = "200", description = "Listado de tipos de reparación calzado")
    public ResponseEntity<List<TipoReparacionCalzadoResponse>> listarTiposCalzado(
            @Parameter(description = "Filtrar por ID de familia (opcional)") @RequestParam(required = false) Long familiaId) {
        return ResponseEntity.ok(catalogoService.listarTiposCalzado(familiaId));
    }

    @GetMapping("/reparaciones-calzado/{id}")
    @Operation(summary = "Buscar tipo de reparación calzado por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoReparacionCalzadoResponse> buscarTipoCalzado(
            @Parameter(description = "ID del tipo de reparación") @PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoCalzado(id));
    }

    @PostMapping("/reparaciones-calzado")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear tipo de reparación calzado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TipoReparacionCalzadoResponse> crearTipoCalzado(@Valid @RequestBody TipoReparacionCalzadoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoCalzado(req));
    }

    @PutMapping("/reparaciones-calzado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar tipo de reparación calzado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo actualizado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoReparacionCalzadoResponse> actualizarTipoCalzado(
            @Parameter(description = "ID del tipo de reparación") @PathVariable Long id,
            @Valid @RequestBody TipoReparacionCalzadoRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoCalzado(id, req));
    }

    @DeleteMapping("/reparaciones-calzado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar tipo de reparación calzado")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo desactivado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<Void> desactivarTipoCalzado(
            @Parameter(description = "ID del tipo de reparación") @PathVariable Long id) {
        catalogoService.desactivarTipoCalzado(id);
        return ResponseEntity.noContent().build();
    }

    // ---- COSTURA ----

    @GetMapping("/costuras")
    @Operation(summary = "Listar tipos de costura")
    @ApiResponse(responseCode = "200", description = "Listado de tipos de costura activos")
    public ResponseEntity<List<TipoCosturaResponse>> listarTiposCostura() {
        return ResponseEntity.ok(catalogoService.listarTiposCostura());
    }

    @GetMapping("/costuras/{id}")
    @Operation(summary = "Buscar tipo de costura por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoCosturaResponse> buscarTipoCostura(
            @Parameter(description = "ID del tipo de costura") @PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoCostura(id));
    }

    @PostMapping("/costuras")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear tipo de costura")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TipoCosturaResponse> crearTipoCostura(@Valid @RequestBody TipoCosturaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoCostura(req));
    }

    @PutMapping("/costuras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar tipo de costura")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo actualizado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoCosturaResponse> actualizarTipoCostura(
            @Parameter(description = "ID del tipo de costura") @PathVariable Long id,
            @Valid @RequestBody TipoCosturaRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoCostura(id, req));
    }

    @DeleteMapping("/costuras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar tipo de costura")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo desactivado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<Void> desactivarTipoCostura(
            @Parameter(description = "ID del tipo de costura") @PathVariable Long id) {
        catalogoService.desactivarTipoCostura(id);
        return ResponseEntity.noContent().build();
    }

    // ---- LLAVES ----

    @GetMapping("/llaves")
    @Operation(summary = "Listar tipos de llave")
    @ApiResponse(responseCode = "200", description = "Listado de tipos de llave activos")
    public ResponseEntity<List<TipoLlaveResponse>> listarTiposLlave() {
        return ResponseEntity.ok(catalogoService.listarTiposLlave());
    }

    @GetMapping("/llaves/{id}")
    @Operation(summary = "Buscar tipo de llave por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoLlaveResponse> buscarTipoLlave(
            @Parameter(description = "ID del tipo de llave") @PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoLlave(id));
    }

    @PostMapping("/llaves")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear tipo de llave")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo creado"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<TipoLlaveResponse> crearTipoLlave(@Valid @RequestBody TipoLlaveRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoLlave(req));
    }

    @PutMapping("/llaves/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar tipo de llave")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo actualizado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<TipoLlaveResponse> actualizarTipoLlave(
            @Parameter(description = "ID del tipo de llave") @PathVariable Long id,
            @Valid @RequestBody TipoLlaveRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoLlave(id, req));
    }

    @DeleteMapping("/llaves/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Desactivar tipo de llave")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo desactivado"),
            @ApiResponse(responseCode = "404", description = "Tipo no encontrado")
    })
    public ResponseEntity<Void> desactivarTipoLlave(
            @Parameter(description = "ID del tipo de llave") @PathVariable Long id) {
        catalogoService.desactivarTipoLlave(id);
        return ResponseEntity.noContent().build();
    }
}
