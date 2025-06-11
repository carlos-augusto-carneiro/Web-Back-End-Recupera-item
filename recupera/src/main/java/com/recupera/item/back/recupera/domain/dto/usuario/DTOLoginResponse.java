package com.recupera.item.back.recupera.domain.dto.usuario;

public record DTOLoginResponse(String acessString, Long expiresIn) {

    public DTOLoginResponse(String acessString, Long expiresIn) {
        this.acessString = acessString;
        this.expiresIn = expiresIn;
    }
}
