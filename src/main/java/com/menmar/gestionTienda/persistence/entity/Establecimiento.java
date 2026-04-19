package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "establecimiento")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Establecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(name = "email_remitente", length = 150)
    private String emailRemitente;

    @Column(name = "callmebot_api_key", length = 100)
    private String callmebotApiKey;

    @Column(name = "telegram_bot_token", length = 100)
    private String telegramBotToken;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "establecimientos")
    @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}
