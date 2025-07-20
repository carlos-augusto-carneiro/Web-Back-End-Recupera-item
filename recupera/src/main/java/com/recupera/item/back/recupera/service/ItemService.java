package com.recupera.item.back.recupera.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recupera.item.back.recupera.domain.dto.usuario.CriarItemDto;
import com.recupera.item.back.recupera.domain.model.usuario.Item;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.domain.repository.IItemRepository;
import com.recupera.item.back.recupera.domain.repository.IUsuarioRepository;

@Service
public class ItemService {

    @Autowired
    private IItemRepository itemRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    public List<Item> listarItensPorUsuario(long usuarioid) {
        return itemRepository.findByUsuarioId(usuarioid);
    }

    public List<Item> listarItensNaoDevolvidos() {
        return itemRepository.findByDevolvido(false);
    }

    public List<Item> listarItensDevolvidos() {
        return itemRepository.findByDevolvido(true);
    }

    public Item adicionarItem(CriarItemDto itemDto, long usuarioIdLogado, String perfil, String url) {
        if (itemDto == null || itemDto.nome() == null || itemDto.nome().isEmpty()) {
            throw new IllegalArgumentException("Dados do item inválidos");
        }
        if (perfil == null || perfil.isEmpty() || perfil.equals("Aluno")) {
            throw new IllegalArgumentException("Perfil do usuário inválido");
        }
        if (usuarioIdLogado == 0) {
            throw new RuntimeException("Usuário não está logado ou não encontrado no cache");
        }
        if (!usuarioRepository.existsById(usuarioIdLogado)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Usuario usuario = usuarioRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Item novoItem = new Item(usuario, itemDto.nome(), itemDto.descricao(), url);
        
        return itemRepository.save(novoItem);
    }    

    public Item marcarItemComoDevolvido(long itemId, long usuarioIdLogado, String perfil) {
        if (usuarioIdLogado == 0) {
            throw new RuntimeException("Usuário não está logado ou não encontrado no cache");
        }
        if (!usuarioRepository.existsById(usuarioIdLogado)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (item.devolvido) {
            throw new IllegalStateException("Item já foi devolvido");
        }

        if (perfil.equals("Aluno")) {
            throw new IllegalArgumentException("Usuário não tem permissão para marcar itens como devolvidos");
        }

        item.marcarComoDevolvido();
        return itemRepository.save(item);
    }

    public void excluirItem(long itemId, long usuarioIdLogado, String perfil) {
        if (usuarioIdLogado == 0) {
            throw new RuntimeException("Usuário não está logado ou não encontrado no cache");
        }
        if (!usuarioRepository.existsById(usuarioIdLogado)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (perfil.equals("Aluno")) {
            throw new IllegalArgumentException("Usuário não tem permissão para excluir itens");
        }

        itemRepository.delete(item);
    }

    public Item buscarItemPorId(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    public List<Item> buscarItensPorNome(String nome) {
        if (nome == null || nome.isEmpty()) {
            throw new IllegalArgumentException("Nome do item não pode ser vazio");
        }
        return itemRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Item atualizarItem(long itemId, CriarItemDto itemDto, long usuarioIdLogado, String perfil) {
        if (usuarioIdLogado == 0) {
            throw new RuntimeException("Usuário não está logado ou não encontrado no cache");
        }
        if (!usuarioRepository.existsById(usuarioIdLogado)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    
        if (perfil.equals("Aluno")) {
            throw new IllegalArgumentException("Usuário não tem permissão para editar itens");
        }
    
        if (itemDto.nome() != null && !itemDto.nome().isEmpty()) {
            item.setNome(itemDto.nome());
        }
        if (itemDto.descricao() != null) {
            item.setDescricao(itemDto.descricao());
        }
    
        return itemRepository.save(item);
    }
}
