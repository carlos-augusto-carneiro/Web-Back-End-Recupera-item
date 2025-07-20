package com.recupera.item.back.recupera.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recupera.item.back.recupera.domain.dto.usuario.CriarItemDto;
import com.recupera.item.back.recupera.domain.model.usuario.Item;
import com.recupera.item.back.recupera.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.recupera.item.back.recupera.config.SecurityConfig;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Item item;
    private CriarItemDto criarItemDto;

    @BeforeEach
    void setUp() {
        item = new Item();
        item.setId(1L);
        item.setNome("Chave");
        item.setDescricao("Chave do portão");
        item.setDevolvido(false);
        criarItemDto = new CriarItemDto("Chave", "Chave do portão", null, false);
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveListarItensPorUsuario() throws Exception {
        Mockito.when(itemService.listarItensPorUsuario(anyLong())).thenReturn(Collections.singletonList(item));
        mockMvc.perform(get("/itens/usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Chave"));
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveBuscarItemPorId() throws Exception {
        Mockito.when(itemService.buscarItemPorId(1L)).thenReturn(item);
        mockMvc.perform(get("/itens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Chave"));
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveAdicionarItem() throws Exception {
        Mockito.when(itemService.adicionarItem(any(), anyLong(), anyString())).thenReturn(item);
        mockMvc.perform(post("/itens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarItemDto))
                .param("usuarioIdLogado", "1")
                .param("perfil", "Administrador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Chave"));
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveAtualizarItem() throws Exception {
        Mockito.when(itemService.atualizarItem(anyLong(), any(), anyLong(), anyString())).thenReturn(item);
        mockMvc.perform(put("/itens/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criarItemDto))
                .param("usuarioIdLogado", "1")
                .param("perfil", "Administrador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Chave"));
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveMarcarItemComoDevolvido() throws Exception {
        item.setDevolvido(true);
        Mockito.when(itemService.marcarItemComoDevolvido(anyLong(), anyLong(), anyString())).thenReturn(item);
        mockMvc.perform(put("/itens/1/devolver")
                .param("usuarioIdLogado", "1")
                .param("perfil", "Administrador"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.devolvido").value(true));
    }

    @Test
    @WithMockUser(authorities = {"Administrador"})
    void deveExcluirItem() throws Exception {
        Mockito.doNothing().when(itemService).excluirItem(anyLong(), anyLong(), anyString());
        mockMvc.perform(delete("/itens/1")
                .param("usuarioIdLogado", "1")
                .param("perfil", "Administrador"))
                .andExpect(status().isNoContent());
    }
} 