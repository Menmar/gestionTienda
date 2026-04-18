package com.menmar.gestionTienda.service.notificacion;

import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import com.menmar.gestionTienda.persistence.entity.Ticket;

public interface CanalNotificacionStrategy {
    CanalNotificacion canal();
    void enviar(Ticket ticket);
}
