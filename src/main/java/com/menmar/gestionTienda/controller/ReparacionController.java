package com.menmar.gestionTienda.controller;


import com.menmar.gestionTienda.model.ReparacionDTO;
import com.menmar.gestionTienda.service.ReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reparacion")
public class ReparacionController {

    @Autowired
    ReparacionService reparacionService;

    /**
     * Crea una nueva reparacion
     *
     * @param reparacionDTO
     */
    @PostMapping
    public ReparacionDTO crearReparacion(@RequestBody ReparacionDTO reparacionDTO) {
        return reparacionService.creaReparacion(reparacionDTO);
    }

    /**
     * Actualiza una reparacion
     *
     * @param reparacionDTO
     */
    @PutMapping
    public ReparacionDTO actualizarReparacion(@RequestBody ReparacionDTO reparacionDTO) {
        return reparacionService.actualizaReparacion(reparacionDTO);
    }

    /**
     * Borra una reparacion
     *
     * @param reparacionDTO
     */
    @DeleteMapping
    public Boolean borrarReparacion(@RequestBody ReparacionDTO reparacionDTO) {
        reparacionService.borraReparacion(reparacionDTO);
        return true;
    }

    /**
     * Consulta todas las reparaciones
     */
    @GetMapping
    public List<ReparacionDTO> consultarReparacion() {
        return reparacionService.consultaReparacion();
    }

    /**
     * Consulta un reparacion por id
     *
     * @param codReparacion
     * @return
     */
    @GetMapping("/{codReparacion}")
    public ReparacionDTO consultarReparacion(@PathVariable String codReparacion) {
        return reparacionService.consultaReparacion(codReparacion);
    }
}
