package com.recupera.item.back.recupera.domain.model.usuario;

import java.util.Date;

import com.recupera.item.back.recupera.domain.enums.Perfis;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    public Date dataCriacao;

    public String nome;
    public String descricao;

    public boolean devolvido;

    public Item() {
        this.dataCriacao = new Date();
        this.devolvido = false;
    }

    public Item(Usuario usuario, String nome, String descricao) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }
        if (usuario.perfil != Perfis.Guarda && usuario.perfil != Perfis.Administrador) {
            throw new IllegalArgumentException("Usuário não tem permissão para criar itens");
        }
        this.usuario = usuario;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = new Date();
        this.devolvido = false;
    }

    public void marcarComoDevolvido() {
        if (this.devolvido) {
            throw new IllegalStateException("Item já foi devolvido");
        }
        this.devolvido = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isDevolvido() {
        return devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }


}
