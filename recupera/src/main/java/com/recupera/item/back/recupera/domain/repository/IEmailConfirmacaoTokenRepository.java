package com.recupera.item.back.recupera.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.recupera.domain.model.usuario.EmailConfirmacaoToken;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;

@Repository
public interface IEmailConfirmacaoTokenRepository extends JpaRepository<EmailConfirmacaoToken, Long> {
    Optional<EmailConfirmacaoToken> findByUsuario(Usuario usuario);
    boolean existsByUsuario(Usuario usuario);
    Optional<EmailConfirmacaoToken> findByToken(String token);
}
