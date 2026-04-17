package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import com.menmar.gestionTienda.service.FotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Fotos", description = "Subida, listado y descarga de imágenes adjuntas a un ticket")
public class FotoController {

    private final FotoService fotoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir foto", description = "Adjunta una imagen (jpg, jpeg, png, webp, gif) al ticket indicado. Tamaño máximo configurable en `spring.servlet.multipart.max-file-size`.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Foto subida correctamente"),
            @ApiResponse(responseCode = "400", description = "Formato de fichero no permitido"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado"),
            @ApiResponse(responseCode = "413", description = "Fichero demasiado grande")
    })
    public ResponseEntity<FotoResponse> subir(
            @Parameter(description = "ID del ticket") @PathVariable Long ticketId,
            @Parameter(description = "Fichero de imagen") @RequestParam("fichero") MultipartFile fichero) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fotoService.subir(ticketId, fichero));
    }

    @GetMapping
    @Operation(summary = "Listar fotos del ticket", description = "Devuelve los metadatos de todas las imágenes adjuntas al ticket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado de fotos"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<List<FotoResponse>> listar(
            @Parameter(description = "ID del ticket") @PathVariable Long ticketId) {
        return ResponseEntity.ok(fotoService.listar(ticketId));
    }

    @GetMapping("/{fotoId}")
    @Operation(summary = "Descargar foto", description = "Devuelve el contenido binario de la imagen para mostrarlo inline en el navegador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen devuelta", content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada o no pertenece al ticket")
    })
    public ResponseEntity<Resource> descargar(
            @Parameter(description = "ID del ticket") @PathVariable Long ticketId,
            @Parameter(description = "ID de la foto") @PathVariable Long fotoId) {
        var recurso = fotoService.descargar(ticketId, fotoId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + recurso.getFilename() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(recurso);
    }

    @DeleteMapping("/{fotoId}")
    @Operation(summary = "Eliminar foto", description = "Borra la imagen del disco y su registro en base de datos.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Foto eliminada"),
            @ApiResponse(responseCode = "404", description = "Foto no encontrada o no pertenece al ticket")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del ticket") @PathVariable Long ticketId,
            @Parameter(description = "ID de la foto") @PathVariable Long fotoId) {
        fotoService.eliminar(ticketId, fotoId);
        return ResponseEntity.noContent().build();
    }
}
