package com.recupera.item.back.recupera.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recupera.item.back.recupera.service.GoogleDriveService;
import com.recupera.item.back.recupera.domain.dto.usuario.CriarItemDto;
import com.recupera.item.back.recupera.domain.model.usuario.Item;
import com.recupera.item.back.recupera.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/itens")
@Tag(name = "Item", description = "Controla os itens do sistema")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private GoogleDriveService googleDriveService;

    private final String GOOGLE_DRIVE= "1HtdhlodxJXfjAnvo06w1tGBiPFzgMST_";

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar itens por usuário", description = "Lista todos os itens associados a um usuário específico")
    @PreAuthorize("hasAuthority('Administrador')")
    public ResponseEntity<?> listarItensPorUsuario(@PathVariable long usuarioId) {
        try {
            return ResponseEntity.ok(itemService.listarItensPorUsuario(usuarioId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/nao-devolvidos")
    @Operation(summary = "Listar itens não devolvidos", description = "Lista todos os itens que ainda não foram devolvidos")
    public ResponseEntity<?> listarItensNaoDevolvidos() {
        try {
            return ResponseEntity.ok(itemService.listarItensNaoDevolvidos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/devolvidos")
    @Operation(summary = "Listar itens devolvidos", description = "Lista todos os itens que já foram devolvidos")
    public ResponseEntity<?> listarItensDevolvidos() {
        try {
            return ResponseEntity.ok(itemService.listarItensDevolvidos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Buscar item por ID", description = "Busca um item específico pelo seu ID")
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
    @Operation(summary = "Buscar itens por nome", description = "Busca itens pelo nome fornecido")
    public ResponseEntity<?> buscarItensPorNome(@RequestParam String nome) {
        try {
            return ResponseEntity.ok(itemService.buscarItensPorNome(nome));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @RequestMapping(path = "/adicionar", method=RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Adicionar item",
        description = "Adiciona um novo item ao sistema"
    )
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    public ResponseEntity<?> adicionarItem(
        @RequestPart("file") MultipartFile file,
        @RequestPart("itemDto") String itemDtoJson,
        Authentication authentication
    ) {
        try {
            System.out.println("itemDtoJson: " + itemDtoJson);
            System.out.println("file: " + (file != null ? file.getOriginalFilename() : "null"));
            ObjectMapper mapper = new ObjectMapper();
            CriarItemDto itemDto = mapper.readValue(itemDtoJson, CriarItemDto.class);
            String url = googleDriveService.uploadFile(file, GOOGLE_DRIVE);
            Long usuarioIdLogado = null;
            String perfil;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
                perfil = jwtAuth.getToken().getClaim("perfil");
            } else {
                usuarioIdLogado = 1L;
                perfil = "Administrador";
            }
            System.out.println("Perfil: " + perfil);
            System.out.println("Usuário ID Logado: " + usuarioIdLogado);
            System.out.println("caminho" + url);
            Item item = itemService.adicionarItem(itemDto, usuarioIdLogado, perfil, url);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{itemId}")
    @Operation(summary = "Atualizar item", description = "Atualiza as informações de um item existente")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    public ResponseEntity<?> atualizarItem(@PathVariable long itemId,
                                              @RequestBody CriarItemDto itemDto
                                              ,Authentication authentication
                                            ) {
        try {
            Long usuarioIdLogado = null;
            String perfil;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
                perfil = jwtAuth.getToken().getClaim("perfil");
            } else {
                usuarioIdLogado = 1L;
                perfil = "Administrador";
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
    @Operation(summary = "Marcar item como devolvido", description = "Marca um item como devolvido no sistema")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    public ResponseEntity<?> marcarItemComoDevolvido(@PathVariable long itemId,
                                                        Authentication authentication
                                                ) {
        try {
            Long usuarioIdLogado = null;
            String perfil;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
                perfil = jwtAuth.getToken().getClaim("perfil");
            } else {
                usuarioIdLogado = 1L;
                perfil = "Administrador";
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
    @Operation(summary = "Excluir item", description = "Exclui um item do sistema")
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Professor') or hasAuthority('Guarda')")
    public ResponseEntity<?> excluirItem(@PathVariable long itemId,
                                            Authentication authentication
                                    ) {
        try {
            Long usuarioIdLogado = null;
            String perfil;
            if (authentication instanceof JwtAuthenticationToken jwtAuth) {
                usuarioIdLogado = jwtAuth.getToken().getClaim("id");
                perfil = jwtAuth.getToken().getClaim("perfil");
            } else {
                usuarioIdLogado = 1L;
                perfil = "Administrador";
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
