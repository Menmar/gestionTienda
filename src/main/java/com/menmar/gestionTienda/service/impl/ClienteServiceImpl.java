package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.exception.ConflictoException;
import com.menmar.gestionTienda.exception.RecursoNoEncontradoException;
import com.menmar.gestionTienda.mapper.ClienteMapper;
import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.cliente.ClienteRequest;
import com.menmar.gestionTienda.model.cliente.ClienteResponse;
import com.menmar.gestionTienda.persistence.entity.Cliente;
import com.menmar.gestionTienda.persistence.repository.ClienteRepository;
import com.menmar.gestionTienda.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    @Transactional
    public ClienteResponse crear(ClienteRequest request) {
        if (clienteRepository.existsByTelefono(request.telefono())) {
            throw new ConflictoException("Ya existe un cliente con el teléfono: " + request.telefono());
        }
        return clienteMapper.toResponse(clienteRepository.save(clienteMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponse> listar(Pageable pageable) {
        return PageResponse.of(clienteRepository.findAll(pageable).map(clienteMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ClienteResponse> buscarPorNombre(String nombre, Pageable pageable) {
        return PageResponse.of(clienteRepository
                .findByNombreContainingIgnoreCaseOrApellidosContainingIgnoreCase(nombre, nombre, pageable)
                .map(clienteMapper::toResponse));
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
                        .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + telefono)));
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
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente", id));
    }
}
