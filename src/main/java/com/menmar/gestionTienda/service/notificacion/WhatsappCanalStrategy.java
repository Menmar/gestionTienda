package com.menmar.gestionTienda.service.notificacion;

import com.menmar.gestionTienda.config.AppProperties;
import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class WhatsappCanalStrategy implements CanalNotificacionStrategy {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.WHATSAPP;
    }

    @Override
    public void enviar(Ticket ticket) {
        var cfg = appProperties.notificaciones();
        if (cfg == null || cfg.callmebotApiKey() == null || cfg.callmebotApiKey().isBlank()) {
            log.warn("Callmebot no configurado, omitiendo notificación WHATSAPP para ticket {}", ticket.getNumeroTicket());
            return;
        }
        var telefono = ticket.getCliente().getTelefono();
        try {
            var url = UriComponentsBuilder
                    .fromHttpUrl(cfg.callmebotBaseUrl())
                    .queryParam("phone",  telefono)
                    .queryParam("text",   construirMensaje(ticket))
                    .queryParam("apikey", cfg.callmebotApiKey())
                    .toUriString();
            restTemplate.getForObject(url, String.class);
            log.info("WhatsApp enviado a {} para ticket {}", telefono, ticket.getNumeroTicket());
        } catch (Exception e) {
            log.error("Error enviando WhatsApp a {} para ticket {}: {}", telefono, ticket.getNumeroTicket(), e.getMessage());
        }
    }

    private String construirMensaje(Ticket ticket) {
        var tienda = ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getNombre() : "la tienda";
        return "Su pedido %s está listo en %s.".formatted(ticket.getNumeroTicket(), tienda);
    }
}
