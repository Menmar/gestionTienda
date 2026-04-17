package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;

import java.util.List;

public interface ClienteService {
    ClienteResponse crear(ClienteRequest request);
    List<ClienteResponse> listar();
    ClienteResponse buscarPorId(Long id);
    ClienteResponse buscarPorTelefono(String telefono);
    ClienteResponse actualizar(Long id, ClienteRequest request);
}
