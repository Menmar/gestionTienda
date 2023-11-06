package com.menmar.gestionTienda.controller;

import com.menmar.gestionTienda.model.FamiliaReparacionDTO;
import com.menmar.gestionTienda.service.FamiliaReparacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/familiaReparacion")
public class FamiliaReparacionController {

    @Autowired
    FamiliaReparacionService familiaReparacionService;

    /**
     * Crea una nueva familia de reparaciones
     *
     * @param familiaReparacionDTO
     */
    @PostMapping
    public FamiliaReparacionDTO crearFamila(@RequestBody FamiliaReparacionDTO familiaReparacionDTO) {
        return familiaReparacionService.creaFamila(familiaReparacionDTO);
    }

    /**
     * Actualiza una familia de reparaciones
     *
     * @param familiaReparacionDTO
     */
    @PutMapping
    public FamiliaReparacionDTO actualizarFamila(@RequestBody FamiliaReparacionDTO familiaReparacionDTO) {
        return familiaReparacionService.actualizaFamila(familiaReparacionDTO);
    }

    /**
     * Borra una familia de reparaciones
     *
     * @param familiaReparacionDTO
     */
    @DeleteMapping
    public Boolean borrarFamila(@RequestBody FamiliaReparacionDTO familiaReparacionDTO) {
        familiaReparacionService.borraFamila(familiaReparacionDTO);
        return true;
    }

    /**
     * Consulta todas las familias de reparaciones
     */
    @GetMapping
    public List<FamiliaReparacionDTO> consultarFamila() {
        return familiaReparacionService.consultaFamila();
    }

    /**
     * Consulta una familia de reparaciones por id
     *
     * @param idFamilia
     * @return
     */
    @GetMapping("/{idFamilia}")
    public FamiliaReparacionDTO consultarFamila(@PathVariable Long idFamilia) {
        return familiaReparacionService.consultaFamila(idFamilia);
    }
}
