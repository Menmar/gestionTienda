package com.menmar.gestionTienda.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "cliente")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 150)
    private String apellidos;

    @Column(nullable = false, unique = true, length = 20)
    private String telefono;

    @Column(length = 255)
    private String email;

    @Column(name = "notif_email", nullable = false)
    @Builder.Default
    private boolean notifEmail = false;

    @Column(name = "notif_whatsapp", nullable = false)
    @Builder.Default
    private boolean notifWhatsapp = false;

    @Column(name = "notif_telegram", nullable = false)
    @Builder.Default
    private boolean notifTelegram = false;

    @Column(name = "telegram_chat_id", length = 50)
    private String telegramChatId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}
