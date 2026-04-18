package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.persistence.entity.Ticket;
import com.menmar.gestionTienda.service.NotificacionService;
import com.menmar.gestionTienda.service.notificacion.CanalNotificacionStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final List<CanalNotificacionStrategy> canales;

    @Override
    @Async
    public void notificarTicketListo(Ticket ticket) {
        var canalesCliente = ticket.getCliente().getCanalesNotificacion();
        canales.stream()
                .filter(c -> canalesCliente.contains(c.canal()))
                .forEach(c -> c.enviar(ticket));
    }
}
