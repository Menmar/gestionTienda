package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.ClienteMapper;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import com.menmar.gestionTienda.persistence.entity.Cliente;
import com.menmar.gestionTienda.persistence.repository.ClienteRepository;
import com.menmar.gestionTienda.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByTelefono(request.telefono())) {
            throw new IllegalArgumentException("Ya existe un cliente con el teléfono: " + request.telefono());
        }
        return clienteMapper.toResponse(clienteRepository.save(clienteMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(clienteMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        return clienteMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteResponse buscarPorTelefono(String telefono) {
        return clienteMapper.toResponse(
                clienteRepository.findByTelefono(telefono)
                        .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + telefono)));
    }

    @Override
    @Transactional
    public ClienteResponse actualizar(Long id, ClienteRequest request) {
        Cliente cliente = findOrThrow(id);
        clienteMapper.updateEntity(request, cliente);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    private Cliente findOrThrow(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente no encontrado: " + id));
    }
}
