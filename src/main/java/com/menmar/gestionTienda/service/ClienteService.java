package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import org.springframework.data.domain.Pageable;

/**
 * Gestión del fichero de clientes de la tienda.
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente. El teléfono debe ser único en el sistema.
     *
     * @param request datos del cliente
     * @return cliente creado con su ID asignado
     */
    ClienteResponse crear(ClienteRequest request);

    /**
     * Devuelve todos los clientes paginados.
     *
     * @param pageable configuración de paginación y ordenación
     * @return página de clientes
     */
    PageResponse<ClienteResponse> listar(Pageable pageable);

    /**
     * Busca un cliente por su ID.
     *
     * @param id identificador del cliente
     * @return datos del cliente
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    ClienteResponse buscarPorId(Long id);

    /**
     * Busca un cliente por su número de teléfono.
     *
     * @param telefono número de teléfono
     * @return datos del cliente
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    ClienteResponse buscarPorTelefono(String telefono);

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id      identificador del cliente a actualizar
     * @param request nuevos datos
     * @return cliente actualizado
     */
    ClienteResponse actualizar(Long id, ClienteRequest request);
}
