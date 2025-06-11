package com.recupera.item.back.recupera.controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginResponse;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuário", description = "Controla os usuários do sistema")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    @Operation(summary = "Login usuarios", description = "Logar como usuario")
    @PostMapping("/login")
    public ResponseEntity<DTOLoginResponse> loginUser(@RequestBody DTOLoginRequest loginDto){
        var user = usuarioService.buscarUsuarioPorEmail(loginDto.email());
        if(user == null || !user.LoginCorrect(loginDto, passwordEncoder)){
            throw new BadCredentialsException("Email ou senha não encontrado");
        }
        var now = Instant.now();
        var expiresIn = 300L;
        var claims = JwtClaimsSet.builder()
                                .issuer("monere-back-end")
                                .subject(user.getId().toString())
                                .claim("authorities", List.of(user.getPerfil().name()))
                                .expiresAt(now.plusSeconds(expiresIn))
                                .issuedAt(now)
                                .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok(new DTOLoginResponse(jwtValue, expiresIn));
    }

    @Transactional
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário no sistema")
    @PostMapping("/createdUser")
    public ResponseEntity<Void> createdUser(@RequestBody DTOCreatedUsuario usuario) {
        try {
            usuarioService.createUsuario(usuario);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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
    public ResponseEntity<Void> deletarUsuario(@RequestBody String email) {
        try {
            usuarioService.deletarUsuarioPorEmail(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Guarda", description = "Promove um usuário para o perfil de Guarda")
    @PutMapping("/promoverGuarda")
    public ResponseEntity<Void> promoverGuarda(@RequestBody String email) {
        try {
            usuarioService.promoverParaGuarda(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Professor", description = "Promove um usuário para o perfil de Professor")
    @PutMapping("/promoverProfessor")
    public ResponseEntity<Void> promoverProfessor(@RequestBody String email) {
        try {
            usuarioService.promoverParaProfessor(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Aluno", description = "Promove um usuário para o perfil de Aluno")
    @PutMapping("/promoverAluno")
    public ResponseEntity<Void> promoverAluno(@RequestBody String email) {
        try {
            usuarioService.promoverParaAluno(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @PreAuthorize("hasAuthority('Administrador')")
    @Operation(summary = "Promover usuário para Administrador", description = "Promove um usuário para o perfil de Administrador")
    @PutMapping("/promoverAdministrador")
    public ResponseEntity<Void> promoverAdministrador(@RequestBody String email) {
        try {
            usuarioService.promoverParaAdministrador(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }    

    @Transactional
    @PreAuthorize("hasAuthority('Administrador') or hasAuthority('Guarda') or hasAuthority('Professor') or hasAuthority('Aluno')")
    @Operation(summary = "Alterar senha do usuário", description = "Altera a senha de um usuário")
    @PutMapping("/upgradeUsuario")
    public ResponseEntity<Void> upgradeUsuario(@RequestBody DTOUpgradeUsuario request) {
        try {
            usuarioService.atualizarUsuario(request.email(), request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
