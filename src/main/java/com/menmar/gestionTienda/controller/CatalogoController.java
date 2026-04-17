package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.catalogo.*;
import com.menmar.gestionTienda.service.CatalogoService;
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
public class CatalogoController {

    private final CatalogoService catalogoService;

    // ---- FAMILIAS ----

    @GetMapping("/familias")
    public ResponseEntity<List<FamiliaReparacionResponse>> listarFamilias() {
        return ResponseEntity.ok(catalogoService.listarFamilias());
    }

    @GetMapping("/familias/{id}")
    public ResponseEntity<FamiliaReparacionResponse> buscarFamilia(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarFamilia(id));
    }

    @PostMapping("/familias")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FamiliaReparacionResponse> crearFamilia(@Valid @RequestBody FamiliaReparacionRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearFamilia(req));
    }

    @PutMapping("/familias/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FamiliaReparacionResponse> actualizarFamilia(@PathVariable Long id,
                                                                        @Valid @RequestBody FamiliaReparacionRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarFamilia(id, req));
    }

    @DeleteMapping("/familias/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarFamilia(@PathVariable Long id) {
        catalogoService.desactivarFamilia(id);
        return ResponseEntity.noContent().build();
    }

    // ---- TIPOS CALZADO ----

    @GetMapping("/reparaciones-calzado")
    public ResponseEntity<List<TipoReparacionCalzadoResponse>> listarTiposCalzado(
            @RequestParam(required = false) Long familiaId) {
        return ResponseEntity.ok(catalogoService.listarTiposCalzado(familiaId));
    }

    @GetMapping("/reparaciones-calzado/{id}")
    public ResponseEntity<TipoReparacionCalzadoResponse> buscarTipoCalzado(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoCalzado(id));
    }

    @PostMapping("/reparaciones-calzado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoReparacionCalzadoResponse> crearTipoCalzado(@Valid @RequestBody TipoReparacionCalzadoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoCalzado(req));
    }

    @PutMapping("/reparaciones-calzado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoReparacionCalzadoResponse> actualizarTipoCalzado(@PathVariable Long id,
                                                                                @Valid @RequestBody TipoReparacionCalzadoRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoCalzado(id, req));
    }

    @DeleteMapping("/reparaciones-calzado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarTipoCalzado(@PathVariable Long id) {
        catalogoService.desactivarTipoCalzado(id);
        return ResponseEntity.noContent().build();
    }

    // ---- COSTURA ----

    @GetMapping("/costuras")
    public ResponseEntity<List<TipoCosturaResponse>> listarTiposCostura() {
        return ResponseEntity.ok(catalogoService.listarTiposCostura());
    }

    @GetMapping("/costuras/{id}")
    public ResponseEntity<TipoCosturaResponse> buscarTipoCostura(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoCostura(id));
    }

    @PostMapping("/costuras")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoCosturaResponse> crearTipoCostura(@Valid @RequestBody TipoCosturaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoCostura(req));
    }

    @PutMapping("/costuras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoCosturaResponse> actualizarTipoCostura(@PathVariable Long id,
                                                                      @Valid @RequestBody TipoCosturaRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoCostura(id, req));
    }

    @DeleteMapping("/costuras/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarTipoCostura(@PathVariable Long id) {
        catalogoService.desactivarTipoCostura(id);
        return ResponseEntity.noContent().build();
    }

    // ---- LLAVES ----

    @GetMapping("/llaves")
    public ResponseEntity<List<TipoLlaveResponse>> listarTiposLlave() {
        return ResponseEntity.ok(catalogoService.listarTiposLlave());
    }

    @GetMapping("/llaves/{id}")
    public ResponseEntity<TipoLlaveResponse> buscarTipoLlave(@PathVariable Long id) {
        return ResponseEntity.ok(catalogoService.buscarTipoLlave(id));
    }

    @PostMapping("/llaves")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoLlaveResponse> crearTipoLlave(@Valid @RequestBody TipoLlaveRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogoService.crearTipoLlave(req));
    }

    @PutMapping("/llaves/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoLlaveResponse> actualizarTipoLlave(@PathVariable Long id,
                                                                   @Valid @RequestBody TipoLlaveRequest req) {
        return ResponseEntity.ok(catalogoService.actualizarTipoLlave(id, req));
    }

    @DeleteMapping("/llaves/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarTipoLlave(@PathVariable Long id) {
        catalogoService.desactivarTipoLlave(id);
        return ResponseEntity.noContent().build();
    }
}
