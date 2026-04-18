package com.menmar.gestionTienda.service.impl;

import com.menmar.gestionTienda.config.AppProperties;
import com.menmar.gestionTienda.persistence.entity.Ticket;
import com.menmar.gestionTienda.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacionServiceImpl implements NotificacionService {

    private final JavaMailSender mailSender;
    private final AppProperties appProperties;
    private final RestTemplate restTemplate;

    @Override
    @Async
    public void notificarTicketListo(Ticket ticket) {
        var cliente = ticket.getCliente();
        var tienda = ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getNombre() : "la tienda";
        var mensaje = "Su pedido %s está listo para recoger en %s. Teléfono: %s".formatted(
                ticket.getNumeroTicket(),
                tienda,
                ticket.getEstablecimiento() != null && ticket.getEstablecimiento().getTelefono() != null
                        ? ticket.getEstablecimiento().getTelefono() : "—");

        if (cliente.isNotifEmail() && cliente.getEmail() != null) {
            enviarEmail(cliente.getEmail(), ticket.getNumeroTicket(), mensaje);
        }
        if (cliente.isNotifWhatsapp()) {
            enviarWhatsapp(cliente.getTelefono(), mensaje);
        }
        if (cliente.isNotifTelegram() && cliente.getTelegramChatId() != null) {
            enviarTelegram(cliente.getTelegramChatId(), mensaje);
        }
    }

    private void enviarEmail(String destinatario, String numeroTicket, String texto) {
        try {
            var msg = new SimpleMailMessage();
            msg.setTo(destinatario);
            msg.setSubject("Su pedido " + numeroTicket + " está listo");
            msg.setText(texto);
            mailSender.send(msg);
            log.info("Email enviado a {}", destinatario);
        } catch (Exception e) {
            log.error("Error enviando email a {}: {}", destinatario, e.getMessage());
        }
    }

    private void enviarWhatsapp(String telefono, String mensaje) {
        try {
            var cfg = appProperties.notificaciones();
            if (cfg == null || cfg.callmebotApiKey() == null || cfg.callmebotApiKey().isBlank()) {
                log.warn("Callmebot no configurado, omitiendo notificación WhatsApp");
                return;
            }
            var url = UriComponentsBuilder
                    .fromHttpUrl("https://api.callmebot.com/whatsapp.php")
                    .queryParam("phone", telefono)
                    .queryParam("text", mensaje)
                    .queryParam("apikey", cfg.callmebotApiKey())
                    .toUriString();
            restTemplate.getForObject(url, String.class);
            log.info("WhatsApp enviado a {}", telefono);
        } catch (Exception e) {
            log.error("Error enviando WhatsApp a {}: {}", telefono, e.getMessage());
        }
    }

    private void enviarTelegram(String chatId, String mensaje) {
        try {
            var cfg = appProperties.notificaciones();
            if (cfg == null || cfg.telegramBotToken() == null || cfg.telegramBotToken().isBlank()) {
                log.warn("Telegram bot no configurado, omitiendo notificación");
                return;
            }
            var url = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s"
                    .formatted(cfg.telegramBotToken(), chatId,
                            mensaje.replace(" ", "%20").replace("&", "%26"));
            restTemplate.getForObject(url, String.class);
            log.info("Telegram enviado a chat {}", chatId);
        } catch (Exception e) {
            log.error("Error enviando Telegram a {}: {}", chatId, e.getMessage());
        }
    }
}
