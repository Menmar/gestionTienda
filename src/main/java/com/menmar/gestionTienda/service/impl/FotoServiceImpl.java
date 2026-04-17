package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.config.AppProperties;
import com.menmar.gestionTienda.model.foto.FotoResponse;
import com.menmar.gestionTienda.persistence.entity.Foto;
import com.menmar.gestionTienda.persistence.repository.FotoRepository;
import com.menmar.gestionTienda.persistence.repository.TicketRepository;
import com.menmar.gestionTienda.service.FotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FotoServiceImpl implements FotoService {

    private final FotoRepository fotoRepository;
    private final TicketRepository ticketRepository;
    private final AppProperties appProperties;

    private static final java.util.Set<String> EXTENSIONES_PERMITIDAS =
            java.util.Set.of(".jpg", ".jpeg", ".png", ".webp", ".gif");

    @Override
    @Transactional
    public FotoResponse subir(Long ticketId, MultipartFile fichero) {
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new NoSuchElementException("Ticket no encontrado: " + ticketId));

        if (fichero.isEmpty()) {
            throw new IllegalArgumentException("El fichero no puede estar vacío");
        }
        var extension = extensionDe(fichero.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension)) {
            throw new IllegalArgumentException(
                    "Tipo de fichero no permitido. Use: " + EXTENSIONES_PERMITIDAS);
        }
        var nombreFichero = UUID.randomUUID() + extension;
        var destino = directorioTicket(ticketId).resolve(nombreFichero);

        try {
            Files.createDirectories(destino.getParent());
            fichero.transferTo(destino);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al guardar la foto", e);
        }

        var foto = Foto.builder()
                .ticket(ticket)
                .rutaLocal(destino.toString())
                .build();

        var guardada = fotoRepository.save(foto);
        return toResponse(guardada, nombreFichero);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FotoResponse> listar(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new NoSuchElementException("Ticket no encontrado: " + ticketId);
        }
        return fotoRepository.findByTicketId(ticketId).stream()
                .map(f -> toResponse(f, Path.of(f.getRutaLocal()).getFileName().toString()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Resource descargar(Long ticketId, Long fotoId) {
        var foto = findFotoDelTicket(ticketId, fotoId);
        try {
            var recurso = new UrlResource(Path.of(foto.getRutaLocal()).toUri());
            if (!recurso.exists()) {
                throw new NoSuchElementException("Fichero no encontrado en disco para foto: " + fotoId);
            }
            return recurso;
        } catch (IOException e) {
            throw new UncheckedIOException("Error al leer la foto", e);
        }
    }

    @Override
    @Transactional
    public void eliminar(Long ticketId, Long fotoId) {
        var foto = findFotoDelTicket(ticketId, fotoId);
        try {
            Files.deleteIfExists(Path.of(foto.getRutaLocal()));
        } catch (IOException e) {
            throw new UncheckedIOException("Error al eliminar la foto del disco", e);
        }
        fotoRepository.delete(foto);
    }

    private Foto findFotoDelTicket(Long ticketId, Long fotoId) {
        var foto = fotoRepository.findById(fotoId)
                .orElseThrow(() -> new NoSuchElementException("Foto no encontrada: " + fotoId));
        if (!foto.getTicket().getId().equals(ticketId)) {
            throw new NoSuchElementException("La foto %d no pertenece al ticket %d".formatted(fotoId, ticketId));
        }
        return foto;
    }

    private Path directorioTicket(Long ticketId) {
        return Path.of(appProperties.fotos().directorio()).resolve(String.valueOf(ticketId));
    }

    private static String extensionDe(String nombreOriginal) {
        if (nombreOriginal == null) return "";
        var idx = nombreOriginal.lastIndexOf('.');
        return idx >= 0 ? nombreOriginal.substring(idx).toLowerCase() : "";
    }

    private static FotoResponse toResponse(Foto foto, String nombreFichero) {
        return new FotoResponse(foto.getId(), foto.getTicket().getId(), nombreFichero, foto.getCreatedAt());
    }
}
