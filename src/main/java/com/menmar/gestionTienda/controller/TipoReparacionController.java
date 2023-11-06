package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.TipoReparacionDTO;
import com.menmar.gestionTienda.service.TipoReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipoReparacion")
public class TipoReparacionController {
    @Autowired
    TipoReparacionService tipoReparacionService;

    /**
     * Crea un nuevo tipo de reparaciones
     *
     * @param tipoReparacionDTO
     */
    @PostMapping
    public TipoReparacionDTO crearTipo(@RequestBody TipoReparacionDTO tipoReparacionDTO) {
        return tipoReparacionService.creaTipo(tipoReparacionDTO);
    }

    /**
     * Actualiza un tipo de reparacion
     *
     * @param tipoReparacionDTO
     */
    @PutMapping
    public TipoReparacionDTO actualizarTipo(@RequestBody TipoReparacionDTO tipoReparacionDTO) {
        return tipoReparacionService.actualizaTipo(tipoReparacionDTO);
    }

    /**
     * Borra un tipo de reparacion
     *
     * @param tipoReparacionDTO
     */
    @DeleteMapping
    public Boolean borrarTipo(@RequestBody TipoReparacionDTO tipoReparacionDTO) {
        tipoReparacionService.borraTipo(tipoReparacionDTO);
        return true;
    }

    /**
     * Consulta todas los tipos de reparaciones
     */
    @GetMapping
    public List<TipoReparacionDTO> consultarTipo() {
        return tipoReparacionService.consultaTipo();
    }

    /**
     * Consulta un tipo de reparacion por id
     *
     * @param idTipo
     * @return
     */
    @GetMapping("/{idTipo}")
    public TipoReparacionDTO consultarTipo(@PathVariable Long idTipo) {
        return tipoReparacionService.consultaTipo(idTipo);
    }
}
