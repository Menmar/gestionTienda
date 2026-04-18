package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.mapper.UsuarioMapper;
import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.usuario.CambioPasswordRequest;
import com.menmar.gestionTienda.model.usuario.ResetPasswordRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioResponse;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import com.menmar.gestionTienda.persistence.repository.UsuarioRepository;
import com.menmar.gestionTienda.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + request.email());
        }
        var usuario = Usuario.builder()
                .nombre(request.nombre())
                .apellidos(request.apellidos())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.passwordInicial()))
                .rol(request.rol())
                .activo(true)
                .build();
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UsuarioResponse> listar(Pageable pageable) {
        return PageResponse.of(usuarioRepository.findAll(pageable).map(usuarioMapper::toResponse));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        return usuarioMapper.toResponse(findOrThrow(id));
    }

    @Override
    @Transactional
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {
        var usuario = findOrThrow(id);
        usuario.setNombre(request.nombre());
        usuario.setApellidos(request.apellidos());
        usuario.setEmail(request.email());
        usuario.setRol(request.rol());
        // La contraseña no se modifica aquí; usar resetPassword o cambiarPassword
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void desactivar(Long id) {
        var usuario = findOrThrow(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void cambiarPassword(String emailEmpleado, CambioPasswordRequest request) {
        var usuario = usuarioRepository.findByEmail(emailEmpleado)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + emailEmpleado));

        if (!passwordEncoder.matches(request.passwordActual(), usuario.getPasswordHash())) {
            throw new BadCredentialsException("La contraseña actual no es correcta");
        }
        usuario.setPasswordHash(passwordEncoder.encode(request.passwordNueva()));
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void resetPassword(Long id, ResetPasswordRequest request) {
        var usuario = findOrThrow(id);
        usuario.setPasswordHash(passwordEncoder.encode(request.passwordNueva()));
        usuarioRepository.save(usuario);
    }

    private Usuario findOrThrow(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + id));
    }
}
