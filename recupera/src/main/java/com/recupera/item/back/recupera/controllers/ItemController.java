package com.recupera.item.back.recupera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recupera.item.back.recupera.domain.dto.usuario.CriarItemDto;
import com.recupera.item.back.recupera.domain.model.usuario.Item;
import com.recupera.item.back.recupera.service.ItemService;


import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itens")
@Tag(name = "Item", description = "Controla os itens do sistema")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> listarItensPorUsuario(@PathVariable long usuarioId) {
        try {
            return ResponseEntity.ok(itemService.listarItensPorUsuario(usuarioId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/nao-devolvidos")
    public ResponseEntity<?> listarItensNaoDevolvidos() {
        try {
            return ResponseEntity.ok(itemService.listarItensNaoDevolvidos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/devolvidos")
    public ResponseEntity<?> listarItensDevolvidos() {
        try {
            return ResponseEntity.ok(itemService.listarItensDevolvidos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{itemId}")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<?> buscarItemPorId(@PathVariable long itemId) {
        try {
            return ResponseEntity.ok(itemService.buscarItemPorId(itemId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarItensPorNome(@RequestParam String nome) {
        try {
            return ResponseEntity.ok(itemService.buscarItensPorNome(nome));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    @Tag(name = "Item", description = "Adiciona um novo item ao sistema")
    public ResponseEntity<?> adicionarItem(@RequestBody CriarItemDto itemDto,
                                            Authentication authentication,
                                               @RequestParam String perfil) {
        try {
            Long usuarioIdLogado = null;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
            } else {
                usuarioIdLogado = 1L;
            }
            System.out.println("Perfil: " + perfil);
            System.out.println("Usuário ID Logado: " + usuarioIdLogado);
            Item item = itemService.adicionarItem(itemDto, usuarioIdLogado, perfil);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{itemId}")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    public ResponseEntity<?> atualizarItem(@PathVariable long itemId,
                                              @RequestBody CriarItemDto itemDto
                                              ,Authentication authentication
                                              ,@RequestParam String perfil) {
        try {
            Long usuarioIdLogado = null;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
            } else {
                usuarioIdLogado = 1L;
            }
            Item item = itemService.atualizarItem(itemId, itemDto, usuarioIdLogado, perfil);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{itemId}/devolver")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    @Tag(name = "Item", description = "Marca um item como devolvido")
    public ResponseEntity<?> marcarItemComoDevolvido(@PathVariable long itemId,
                                                        Authentication authentication,
                                                        @RequestParam String perfil) {
        try {
            Long usuarioIdLogado = null;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
            } else {
                usuarioIdLogado = 1L;
            }
            Item item = itemService.marcarItemComoDevolvido(itemId, usuarioIdLogado, perfil);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    @Tag(name = "Item", description = "Exclui um item do sistema")
    public ResponseEntity<?> excluirItem(@PathVariable long itemId,
                                            Authentication authentication,
                                            @RequestParam String perfil) {
        try {
            Long usuarioIdLogado = null;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
            } else {
                usuarioIdLogado = 1L;
            }
            itemService.excluirItem(itemId, usuarioIdLogado, perfil);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
