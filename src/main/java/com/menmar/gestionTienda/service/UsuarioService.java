package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.model.PageResponse;
import com.menmar.gestionTienda.model.usuario.CambioPasswordRequest;
import com.menmar.gestionTienda.model.usuario.ResetPasswordRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioRequest;
import com.menmar.gestionTienda.model.usuario.UsuarioResponse;
import org.springframework.data.domain.Pageable;

/**
 * Gestión de usuarios del sistema (empleados y administradores).
 */
public interface UsuarioService {

    /**
     * Crea un nuevo usuario con la contraseña inicial proporcionada.
     *
     * @param request datos del usuario (incluye {@code passwordInicial})
     * @return usuario creado con su ID asignado
     */
    UsuarioResponse crear(UsuarioRequest request);

    /**
     * Devuelve todos los usuarios paginados.
     *
     * @param pageable configuración de paginación y ordenación
     * @return página de usuarios
     */
    PageResponse<UsuarioResponse> listar(Pageable pageable);

    /**
     * Busca un usuario por su ID.
     *
     * @param id identificador del usuario
     * @return datos del usuario
     * @throws jakarta.persistence.EntityNotFoundException si no existe
     */
    UsuarioResponse buscarPorId(Long id);

    /**
     * Actualiza nombre, apellidos, email y rol del usuario. No modifica la contraseña.
     *
     * @param id      identificador del usuario
     * @param request nuevos datos (el campo {@code passwordInicial} se ignora)
     * @return usuario actualizado
     */
    UsuarioResponse actualizar(Long id, UsuarioRequest request);

    /**
     * Marca al usuario como inactivo (soft delete). No elimina el registro.
     *
     * @param id identificador del usuario
     */
    void desactivar(Long id);

    /**
     * Permite al propio empleado cambiar su contraseña verificando la actual.
     *
     * @param emailEmpleado email del empleado autenticado
     * @param request       contraseña actual y nueva contraseña
     * @throws IllegalArgumentException si la contraseña actual no coincide
     */
    void cambiarPassword(String emailEmpleado, CambioPasswordRequest request);

    /**
     * El administrador fuerza una nueva contraseña para cualquier usuario
     * sin necesitar la contraseña actual.
     *
     * @param id      identificador del usuario
     * @param request nueva contraseña
     */
    void resetPassword(Long id, ResetPasswordRequest request);
}
