package com.recupera.item.back.recupera.domain.dto.usuario;

import com.recupera.item.back.recupera.domain.enums.Perfis;

import io.swagger.v3.oas.annotations.media.Schema;

public record DTOCreatedUsuario(String nome, String email, String senha, @Schema(defaultValue = "Aluno") Perfis perfil) {

    public DTOCreatedUsuario(String nome, String email, String senha, Perfis perfil) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil != null ? perfil : Perfis.Aluno; 
    }

}
