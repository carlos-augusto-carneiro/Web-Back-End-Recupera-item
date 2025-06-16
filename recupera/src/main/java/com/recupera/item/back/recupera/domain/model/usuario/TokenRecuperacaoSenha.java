package com.recupera.item.back.recupera.domain.model.usuario;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "token_recuperacao_senha")
public class TokenRecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime dataExpiracao;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    

    public TokenRecuperacaoSenha() {
    }

    public boolean estaExpirado() {
        return dataExpiracao.isBefore(LocalDateTime.now());
    }

    public boolean pertenceAoUsuario(Usuario usuario) {
        return this.usuario.equals(usuario);
    }

    public static TokenRecuperacaoSenha gerarPara(Usuario usuario) {
        TokenRecuperacaoSenha token = new TokenRecuperacaoSenha();
        token.token = java.util.UUID.randomUUID().toString();
        token.dataExpiracao = LocalDateTime.now().plusHours(1);
        token.usuario = usuario;
        return token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getToken() {
        return token;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
