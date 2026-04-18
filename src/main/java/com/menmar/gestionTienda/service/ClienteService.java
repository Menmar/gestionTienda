package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);
    PageResponse<ClienteResponse> listar(Pageable pageable);
    PageResponse<ClienteResponse> buscarPorNombre(String nombre, Pageable pageable);
    ClienteResponse buscarPorId(Long id);
    ClienteResponse buscarPorTelefono(String telefono);
    ClienteResponse actualizar(Long id, ClienteRequest request);
}
