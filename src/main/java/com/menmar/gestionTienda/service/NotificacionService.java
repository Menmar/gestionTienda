package com.menmar.gestionTienda.service;

import com.menmar.gestionTienda.persistence.entity.Ticket;

public interface NotificacionService {
    void notificarTicketListo(Ticket ticket);
}
