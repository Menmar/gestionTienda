package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import com.menmar.gestionTienda.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/fotos")
@RequiredArgsConstructor
public class FotoController {

    private final FotoService fotoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FotoResponse> subir(@PathVariable Long ticketId,
                                              @RequestParam("fichero") MultipartFile fichero) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoService.subir(ticketId, fichero));
    }

    @GetMapping
    public ResponseEntity<List<FotoResponse>> listar(@PathVariable Long ticketId) {
        return ResponseEntity.ok(fotoService.listar(ticketId));
    }

    @GetMapping("/{fotoId}")
    public ResponseEntity<Resource> descargar(@PathVariable Long ticketId,
                                              @PathVariable Long fotoId) {
        var recurso = fotoService.descargar(ticketId, fotoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + recurso.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(recurso);
    }

    @DeleteMapping("/{fotoId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long ticketId,
                                         @PathVariable Long fotoId) {
        fotoService.eliminar(ticketId, fotoId);
        return ResponseEntity.noContent().build();
    }
}
