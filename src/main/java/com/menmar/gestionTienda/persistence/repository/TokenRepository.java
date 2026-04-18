package com.menmar.gestionTienda.persistence.repository;

import com.menmar.gestionTienda.persistence.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String token);

    @Query("SELECT t FROM Token t WHERE t.usuario.id = :usuarioId AND t.revocado = false")
    List<Token> findTokensActivosByUsuario(Long usuarioId);

    @Modifying
    @Query("UPDATE Token t SET t.revocado = true WHERE t.usuario.id = :usuarioId AND t.revocado = false")
    void revocarTokensDeUsuario(Long usuarioId);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expiracion < :ahora")
    int deleteByExpiracionBefore(OffsetDateTime ahora);
}
