package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.ticket.CambioEstadoRequest;
import com.menmar.gestionTienda.model.ticket.TicketRequest;
import com.menmar.gestionTienda.model.ticket.TicketResponse;
import com.menmar.gestionTienda.persistence.entity.EstadoTicket;
import com.menmar.gestionTienda.persistence.entity.TipoTicket;
import org.springframework.data.domain.Pageable;

/**
 * Operativa principal de la tienda: apertura de tickets, consulta y cambio de estado.
 */
public interface TicketService {

    /**
     * Abre un nuevo ticket y calcula el precio total a partir de las líneas.
     *
     * @param request       datos del ticket y sus líneas de detalle
     * @param emailEmpleado email del empleado autenticado que abre el ticket
     * @return ticket creado con su número alfanumérico generado
     */
    TicketResponse crear(TicketRequest request, String emailEmpleado);

    /**
     * Busca un ticket por su ID interno.
     *
     * @param id identificador del ticket
     * @return datos completos del ticket
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    TicketResponse buscarPorId(Long id);

    /**
     * Busca un ticket por el código impreso en el resguardo (ej. {@code TK-20240101-0001}).
     *
     * @param numero código alfanumérico del ticket
     * @return datos completos del ticket
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    TicketResponse buscarPorNumero(String numero);

    /**
     * Devuelve tickets paginados con filtros opcionales combinados.
     * Cualquier parámetro puede ser {@code null} para ignorar ese filtro.
     *
     * @param estado    filtrar por estado del ticket
     * @param tipo      filtrar por tipo de trabajo
     * @param clienteId filtrar por cliente
     * @param pageable  configuración de paginación y ordenación
     * @return página de tickets que cumplen los filtros
     */
    PageResponse<TicketResponse> listar(EstadoTicket estado, TipoTicket tipo, Long clienteId, Pageable pageable);

    /**
     * Actualiza el estado de un ticket siguiendo el ciclo de vida:
     * ABIERTO → EN_PROCESO → LISTO → ENTREGADO (o CANCELADO en cualquier punto).
     *
     * @param id      identificador del ticket
     * @param request nuevo estado
     * @return ticket con el estado actualizado
     */
    TicketResponse cambiarEstado(Long id, CambioEstadoRequest request);
}
