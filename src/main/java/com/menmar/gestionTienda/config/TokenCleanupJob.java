package com.menmar.gestionTienda.config;

import com.menmar.gestionTienda.persistence.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupJob {

    private final TokenRepository tokenRepository;

    @Scheduled(cron = "0 0 3 * * SUN")
    @Transactional
    public void limpiarTokensExpirados() {
        var eliminados = tokenRepository.deleteByExpiracionBefore(OffsetDateTime.now());
        log.info("Tokens expirados eliminados: {}", eliminados);
    }
}
