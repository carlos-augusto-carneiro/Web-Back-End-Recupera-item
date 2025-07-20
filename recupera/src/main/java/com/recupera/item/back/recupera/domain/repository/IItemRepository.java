package com.recupera.item.back.recupera.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.recupera.item.back.recupera.domain.model.usuario.Item;

@Repository
public interface IItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUsuarioId(Long usuarioId);
    List<Item> findByDevolvido(boolean devolvido);
    List<Item> findByUsuarioIdAndDevolvido(Long usuarioId, boolean devolvido);
    List<Item> findByNomeContainingIgnoreCase(String nome);

}
