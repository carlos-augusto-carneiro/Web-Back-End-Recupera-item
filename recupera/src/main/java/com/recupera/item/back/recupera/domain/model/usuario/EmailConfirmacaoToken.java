package com.recupera.item.back.recupera.domain.model.usuario;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;


@Entity
public class EmailConfirmacaoToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private LocalDateTime dataExpiracao;

    @OneToOne
    private Usuario usuario;

    protected EmailConfirmacaoToken() {
    }

    public static EmailConfirmacaoToken criarPara(Usuario usuario) {
        EmailConfirmacaoToken token = new EmailConfirmacaoToken();
        token.token = UUID.randomUUID().toString();
        token.usuario = usuario;
        token.dataExpiracao = LocalDateTime.now().plusHours(24);
        return token;
    }

    public boolean estaExpirado() {
        return dataExpiracao.isBefore(LocalDateTime.now());
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }
    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
