package com.recupera.item.back.recupera.service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.recupera.item.back.recupera.domain.model.usuario.TokenRecuperacaoSenha;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.domain.repository.ITokenRecuperacaoSenha;
import com.recupera.item.back.recupera.domain.repository.IUsuarioRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
public class TokenRecuperacaoSenhaService {

    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private ITokenRecuperacaoSenha tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CorpoEmailService corpoEmailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    public TokenRecuperacaoSenhaService(IUsuarioRepository usuarioRepository, ITokenRecuperacaoSenha tokenRepository, EmailService emailService, CorpoEmailService corpoEmailService, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
        this.corpoEmailService = corpoEmailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Async("taskExecutor")
    public CompletableFuture<Void> solicitarRecuperacao(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        String token = UUID.randomUUID().toString();
        LocalDateTime expiracao = LocalDateTime.now().plusHours(1);

        TokenRecuperacaoSenha tokenSenha = new TokenRecuperacaoSenha();
        tokenSenha.setToken(token);
        tokenSenha.setUsuario(usuario);
        tokenSenha.setDataExpiracao(expiracao);

        tokenRepository.save(tokenSenha);

        String link = "http://localhost:3000/recuperar?token=" + token;
        String corpo = corpoEmailService.gerarCorpoEmailRecuperacao(link);

        emailService.enviarEmailRecuperacao(email, "Recuperação de senha", corpo);
        return CompletableFuture.completedFuture(null);
    }

    public void redefinirSenha(String token, String novaSenha) {
        TokenRecuperacaoSenha tokenSenha = tokenRepository.findByToken(token)
            .orElseThrow(() -> new EntityNotFoundException("Token inválido"));

        if (tokenSenha.getDataExpiracao().isBefore(LocalDateTime.now())) {
            throw new EntityNotFoundException("Token expirado");
        }

        Usuario usuario = tokenSenha.getUsuario();
        String senhaCodificada = passwordEncoder.encode(novaSenha);
        usuario.setSenha(senhaCodificada); 

        usuarioRepository.save(usuario);

        tokenRepository.delete(tokenSenha);
    }
        
}
