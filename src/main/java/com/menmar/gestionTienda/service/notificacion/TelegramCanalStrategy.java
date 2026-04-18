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
public class TelegramCanalStrategy implements CanalNotificacionStrategy {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.TELEGRAM;
    }

    @Override
    public void enviar(Ticket ticket) {
        var cfg = appProperties.notificaciones();
        if (cfg == null || cfg.telegramBotToken() == null || cfg.telegramBotToken().isBlank()) {
            log.warn("Telegram bot no configurado, omitiendo notificación para ticket {}", ticket.getNumeroTicket());
            return;
        }
        var chatId = ticket.getCliente().getTelegramChatId();
        if (chatId == null || chatId.isBlank()) {
            log.warn("Cliente {} sin telegram_chat_id, omitiendo notificación TELEGRAM", ticket.getCliente().getId());
            return;
        }
        try {
            var url = UriComponentsBuilder
                    .fromHttpUrl(cfg.telegramBaseUrl() + "/bot" + cfg.telegramBotToken() + "/sendMessage")
                    .queryParam("chat_id", chatId)
                    .queryParam("text",    construirMensaje(ticket))
                    .toUriString();
            restTemplate.getForObject(url, String.class);
            log.info("Telegram enviado a chat {} para ticket {}", chatId, ticket.getNumeroTicket());
        } catch (Exception e) {
            log.error("Error enviando Telegram a chat {} para ticket {}: {}", chatId, ticket.getNumeroTicket(), e.getMessage());
        }
    }

    private String construirMensaje(Ticket ticket) {
        var tienda = ticket.getEstablecimiento() != null ? ticket.getEstablecimiento().getNombre() : "la tienda";
        return "Su pedido %s está listo en %s.".formatted(ticket.getNumeroTicket(), tienda);
    }
}
