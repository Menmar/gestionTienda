package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.UsuarioDTO;
import com.menmar.gestionTienda.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    /**
     * Crea un nuevo usuario
     *
     * @param usuarioDTO
     */
    @PostMapping
    public UsuarioDTO crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.creaUsuario(usuarioDTO);
    }

    /**
     * Actualiza un usuario
     *
     * @param usuarioDTO
     */
    @PutMapping
    public UsuarioDTO actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        return usuarioService.actualizaUsuario(usuarioDTO);
    }

    /**
     * Borra un usuario
     *
     * @param usuarioDTO
     */
    @DeleteMapping
    public Boolean borrarUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        usuarioService.borraUsuario(usuarioDTO);
        return true;
    }

    /**
     * Consulta todos los usuarios
     */
    @GetMapping
    public List<UsuarioDTO> consultarUsuario() {
        return usuarioService.consultaUsuario();
    }

    /**
     * Consulta un usuario por id
     *
     * @param idUsuario
     * @return
     */
    @GetMapping("/{idUsuario}")
    public UsuarioDTO consultarUsuario(@PathVariable String idUsuario) {
        return usuarioService.consultaUsuario(idUsuario);
    }
}
