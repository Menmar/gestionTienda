package com.menmar.gestionTienda.config;

import com.menmar.gestionTienda.persistence.entity.RolUsuario;
import com.menmar.gestionTienda.persistence.entity.Usuario;
import com.menmar.gestionTienda.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppProperties appProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        var adminCfg = appProperties.admin();
        if (adminCfg == null || adminCfg.email() == null || adminCfg.email().isBlank()) {
            return;
        }
        if (usuarioRepository.existsByEmail(adminCfg.email())) {
            return;
        }
        var admin = Usuario.builder()
                .nombre(adminCfg.nombre() != null ? adminCfg.nombre() : "Admin")
                .apellidos(adminCfg.apellidos() != null ? adminCfg.apellidos() : "Sistema")
                .email(adminCfg.email())
                .passwordHash(passwordEncoder.encode(adminCfg.password()))
                .rol(RolUsuario.ADMIN)
                .activo(true)
                .build();
        usuarioRepository.save(admin);
        log.info("Admin inicial creado: {}", adminCfg.email());
    }
}
