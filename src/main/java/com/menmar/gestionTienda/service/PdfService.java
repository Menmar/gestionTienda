package com.menmar.gestionTienda.service;

/**
 * Generación de documentos PDF para la tienda.
 */
public interface PdfService {

    /**
     * Genera el resguardo A5 de un ticket listo para imprimir.
     * Incluye cabecera de la tienda, código del ticket, datos del cliente,
     * tabla de líneas con precios, total y observaciones.
     *
     * @param ticketId identificador del ticket
     * @return bytes del documento PDF generado
     * @throws jakarta.persistence.EntityNotFoundException si el ticket no existe
     */
    byte[] generarResguardoTicket(Long ticketId);
}
