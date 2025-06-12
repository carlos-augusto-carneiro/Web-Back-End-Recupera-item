package com.recupera.item.back.recupera.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.recupera.item.back.recupera.domain.model.usuario.EmailConfirmacaoToken;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.domain.repository.IEmailConfirmacaoTokenRepository;

/**
 * Serviço responsável pelo gerenciamento de tokens de confirmação de e-mail.
 * 
 * Este serviço permite criar, enviar, buscar e remover tokens de confirmação de e-mail associados a usuários.
 * 
 * Funcionalidades principais:
 * <ul>
 *   <li>Geração e envio de token de confirmação para o e-mail do usuário.</li>
 *   <li>Atualização do token caso já exista um para o usuário.</li>
 *   <li>Busca de token pelo valor do token.</li>
 *   <li>Remoção de token.</li>
 * </ul>
 * 
 * Dependências:
 * <ul>
 *   <li>{@link IEmailConfirmacaoTokenRepository} para persistência dos tokens.</li>
 *   <li>{@link EmailService} para envio de e-mails.</li>
 * </ul>
 * 
 * Exceções:
 * <ul>
 *   <li>Lança {@link IllegalArgumentException} se o e-mail do usuário for nulo ou vazio ao tentar enviar confirmação.</li>
 * </ul>
*/

@Service
public class EmailConfirmacaoTokenService {

    private final IEmailConfirmacaoTokenRepository emailConfirmacaoTokenRepository;
    private final EmailService emailService;

    public EmailConfirmacaoTokenService(IEmailConfirmacaoTokenRepository emailConfirmacaoTokenRepository, EmailService emailService) {
        this.emailConfirmacaoTokenRepository = emailConfirmacaoTokenRepository;
        this.emailService = emailService;
    }

    public void enviarConfirmacaoEmail(Usuario usuario) {
        var token = EmailConfirmacaoToken.criarPara(usuario);
        
        // Verifica se já existe um token para este usuário
        emailConfirmacaoTokenRepository.findByUsuario(usuario)
            .ifPresent(existingToken -> {
                existingToken.setToken(token.getToken());
                existingToken.setDataExpiracao(token.getDataExpiracao());
                emailConfirmacaoTokenRepository.save(existingToken);
            });
            
        // Se não existir, salva o novo token
        if (!emailConfirmacaoTokenRepository.existsByUsuario(usuario)) {
            emailConfirmacaoTokenRepository.save(token);
        }

        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O email do usuário não pode ser nulo ou vazio.");
        }
        String link = "http://localhost:8080/confirmar?token=" + token.getToken();
        emailService.enviarEmail(usuario.getEmail(), "Confirme seu e-mail", "Clique no link para confirmar: " + link);
    }

    public Optional<EmailConfirmacaoToken> findByToken(String token) {
        return emailConfirmacaoTokenRepository.findByToken(token);
    }

    public void delete(EmailConfirmacaoToken token) {
        emailConfirmacaoTokenRepository.delete(token);
    }
}
