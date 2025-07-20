package com.recupera.item.back.recupera.controllers;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginResponse;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;
import com.recupera.item.back.recupera.service.EmailConfirmacaoTokenService;
import com.recupera.item.back.recupera.service.TokenRecuperacaoSenhaService;
import com.recupera.item.back.recupera.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuário", description = "Controla os usuários do sistema")
@RestController
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final EmailConfirmacaoTokenService emailConfirmacaoTokenService;
    private final TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, EmailConfirmacaoTokenService emailConfirmacaoTokenService, TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.emailConfirmacaoTokenService = emailConfirmacaoTokenService;
        this.tokenRecuperacaoSenhaService = tokenRecuperacaoSenhaService;
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
                                .issuer("recupera-item")
                                .subject(user.getId().toString())
                                .claim("authorities", List.of(user.getPerfil().name()))
                                .claim("id", user.getId())
                                .claim("email", user.getEmail())
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
            var createdUsuario = usuarioService.createUsuario(usuario);
            emailConfirmacaoTokenService.enviarConfirmacaoEmail(createdUsuario);
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
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            usuarioService.atualizarUsuario(email, request);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Transactional
    @Operation(summary = "Confirmar email", description = "Confirma o email do usuário usando o token")
    @PutMapping("/confirmar")
    public ResponseEntity<String> confirmarEmail(@RequestParam String token) {
        try {
            var emailToken = emailConfirmacaoTokenService.findByToken(token)
                .orElseThrow(() -> new UsuarioException("Token inválido"));

            if (emailToken.estaExpirado()) {
                throw new UsuarioException("Token expirado");
            }

            var usuario = emailToken.getUsuario();
            usuario.setEmailConfirmado(true);
            usuarioService.save(usuario);
            emailConfirmacaoTokenService.delete(emailToken);

            return ResponseEntity.ok("Email confirmado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional
    @Operation(summary = "Esqueci minha senha", description = "Envia um email para o usuário para recuperar a senha")
    @PostMapping("/esqueci-senha")
    public ResponseEntity<Void> esqueciSenha(@RequestBody String email) {
        tokenRecuperacaoSenhaService.solicitarRecuperacao(email);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @Operation(summary = "Redefinir senha", description = "Redefine a senha do usuário usando o token")
    @PutMapping("/redefinir-senha")
    public ResponseEntity<Void> redefinirSenha(@RequestBody String token, @RequestBody String novaSenha) {
        tokenRecuperacaoSenhaService.redefinirSenha(token, novaSenha);
        return ResponseEntity.ok().build();
    }
}
