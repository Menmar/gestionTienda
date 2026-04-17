package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.foto.FotoResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FotoService {
    FotoResponse subir(Long ticketId, MultipartFile fichero);
    List<FotoResponse> listar(Long ticketId);
    Resource descargar(Long ticketId, Long fotoId);
    void eliminar(Long ticketId, Long fotoId);
}
