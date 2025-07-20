package com.recupera.item.back.recupera.domain.dto.usuario;

import java.util.Date;

public record CriarItemDto(String nome, String descricao, Date dataCriacao, Boolean devolvido) {

}