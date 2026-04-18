package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Gestión de imágenes adjuntas a un ticket (subida, listado, descarga y eliminación).
 */
public interface FotoService {

    /**
     * Guarda un fichero de imagen en disco y registra sus metadatos en base de datos.
     * Extensiones permitidas: jpg, jpeg, png, webp, gif.
     *
     * @param ticketId identificador del ticket al que se adjunta la foto
     * @param fichero  fichero de imagen recibido en el multipart
     * @return metadatos de la foto guardada
     * @throws IllegalArgumentException si la extensión del fichero no está permitida
     * @throws jakarta.persistence.EntityNotFoundException si el ticket no existe
     */
    FotoResponse subir(Long ticketId, MultipartFile fichero);

    /**
     * Devuelve los metadatos de todas las fotos adjuntas a un ticket.
     *
     * @param ticketId identificador del ticket
     * @return lista de metadatos de fotos (puede estar vacía)
     */
    List<FotoResponse> listar(Long ticketId);

    /**
     * Devuelve el contenido binario de una foto como {@link Resource} descargable.
     *
     * @param ticketId identificador del ticket
     * @param fotoId   identificador de la foto
     * @return recurso con el contenido del fichero
     * @throws jakarta.persistence.EntityNotFoundException si la foto no existe o no pertenece al ticket
     */
    Resource descargar(Long ticketId, Long fotoId);

    /**
     * Elimina la imagen del disco y su registro en base de datos.
     *
     * @param ticketId identificador del ticket
     * @param fotoId   identificador de la foto
     * @throws jakarta.persistence.EntityNotFoundException si la foto no existe o no pertenece al ticket
     */
    void eliminar(Long ticketId, Long fotoId);
}
