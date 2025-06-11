package com.recupera.item.back.recupera.domain.dto.usuario;

public record DTOLoginRequest(String email, String senha) {

    public DTOLoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

}
