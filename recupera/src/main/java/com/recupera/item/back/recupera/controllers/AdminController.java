package com.recupera.item.back.recupera.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.recupera.item.back.recupera.domain.dto.usuario.DeletarUsuario;

import com.recupera.item.back.recupera.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Administrador", description = "Controla dos administradores do sistema")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioService usuarioService;


    public AdminController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
        
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários do sistema")
    @GetMapping("/listarUsuarios")
    public ResponseEntity<?> listarUsuarios() {
        try {
            return ResponseEntity.ok(usuarioService.listarUsuarios());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Deletar usuário", description = "Deleta um usuário pelo email")
    @DeleteMapping("/deletarUsuario")
    public ResponseEntity<Void> deletarUsuario(@RequestBody DeletarUsuario deletarUsuario) {
        try {
            if (deletarUsuario == null || deletarUsuario.Email() == null || deletarUsuario.Email().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioService.deletarUsuarioPorEmail(deletarUsuario.Email());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Guarda", description = "Promove um usuário para o perfil de Guarda")
    @PutMapping("/promoverGuarda")
    public ResponseEntity<Void> promoverGuarda(@RequestBody DeletarUsuario deletarUsuario) {
        try {
            if (deletarUsuario == null || deletarUsuario.Email() == null || deletarUsuario.Email().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioService.promoverParaGuarda(deletarUsuario.Email());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Professor", description = "Promove um usuário para o perfil de Professor")
    @PutMapping("/promoverProfessor")
    public ResponseEntity<Void> promoverProfessor(@RequestBody DeletarUsuario deletarUsuario) {
        try {
            if (deletarUsuario == null || deletarUsuario.Email() == null || deletarUsuario.Email().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioService.promoverParaProfessor(deletarUsuario.Email());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Aluno", description = "Promove um usuário para o perfil de Aluno")
    @PutMapping("/promoverAluno")
    public ResponseEntity<Void> promoverAluno(@RequestBody DeletarUsuario deletarUsuario) {
        try {
            if (deletarUsuario == null || deletarUsuario.Email() == null || deletarUsuario.Email().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioService.promoverParaAluno(deletarUsuario.Email());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Administrador", description = "Promove um usuário para o perfil de Administrador")
    @PutMapping("/promoverAdministrador")
    public ResponseEntity<Void> promoverAdministrador(@RequestBody DeletarUsuario deletarUsuario) {
        try {
            if (deletarUsuario == null || deletarUsuario.Email() == null || deletarUsuario.Email().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            usuarioService.promoverParaAdministrador(deletarUsuario.Email());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }    
}
