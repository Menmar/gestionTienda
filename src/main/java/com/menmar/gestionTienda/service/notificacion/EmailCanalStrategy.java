package com.menmar.gestionTienda.service.notificacion;

import com.menmar.gestionTienda.config.AppProperties;
import com.menmar.gestionTienda.persistence.entity.CanalNotificacion;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailCanalStrategy implements CanalNotificacionStrategy {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.EMAIL;
    }

    @Override
    public void enviar(Ticket ticket) {
        var email = ticket.getCliente().getEmail();
        if (email == null || email.isBlank()) {
            log.warn("Cliente {} sin email, omitiendo notificación EMAIL", ticket.getCliente().getId());
            return;
        }
        try {
            var remitente = resolverRemitente(ticket);
            var msg = new SimpleMailMessage();
            if (remitente != null) msg.setFrom(remitente);
            msg.setTo(email);
            msg.setSubject("Su pedido %s está listo".formatted(ticket.getNumeroTicket()));
            msg.setText(construirMensaje(ticket));
            mailSender.send(msg);
            log.info("Email enviado a {} para ticket {}", email, ticket.getNumeroTicket());
        } catch (Exception e) {
            log.error("Error enviando email a {} para ticket {}: {}", email, ticket.getNumeroTicket(), e.getMessage());
        }
    }

    private String resolverRemitente(Ticket ticket) {
        var estab = ticket.getEstablecimiento();
        if (estab != null && estab.getEmailRemitente() != null && !estab.getEmailRemitente().isBlank()) {
            return estab.getEmailRemitente();
        }
        var cfg = appProperties.notificaciones();
        return cfg != null ? cfg.emailRemitente() : null;
    }

    private String construirMensaje(Ticket ticket) {
        var tienda = ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getNombre() : "la tienda";
        var tel    = ticket.getEstablecimiento() != null && ticket.getEstablecimiento().getTelefono() != null
                     ? ticket.getEstablecimiento().getTelefono() : "—";
        return """
                Su pedido %s está listo para recoger en %s.
                Teléfono: %s
                """.formatted(ticket.getNumeroTicket(), tienda, tel);
    }
}
