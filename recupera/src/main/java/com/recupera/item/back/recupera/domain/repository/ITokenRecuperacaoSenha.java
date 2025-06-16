package com.recupera.item.back.recupera.domain.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.recupera.domain.model.usuario.TokenRecuperacaoSenha;

@Repository
public interface ITokenRecuperacaoSenha extends JpaRepository<TokenRecuperacaoSenha, Long> {

    Optional<TokenRecuperacaoSenha> findByToken(String token);

}
