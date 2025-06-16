package com.recupera.item.back.recupera.controllers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginResponse;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.model.usuario.EmailConfirmacaoToken;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.service.EmailConfirmacaoTokenService;
import com.recupera.item.back.recupera.service.TokenRecuperacaoSenhaService;
import com.recupera.item.back.recupera.service.UsuarioService;

class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private EmailConfirmacaoTokenService emailConfirmacaoTokenService;
    private TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);
        emailConfirmacaoTokenService = mock(EmailConfirmacaoTokenService.class);
        tokenRecuperacaoSenhaService = mock(TokenRecuperacaoSenhaService.class);
        usuarioController = new UsuarioController(usuarioService, passwordEncoder, jwtEncoder, emailConfirmacaoTokenService, tokenRecuperacaoSenhaService);
    }

    @Test
    void loginUser_shouldReturnJwt_whenCredentialsAreCorrect() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        DTOLoginRequest loginRequest = new DTOLoginRequest(email, password);

        Usuario user = mock(Usuario.class);
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(user);
        when(user.LoginCorrect(eq(loginRequest), eq(passwordEncoder))).thenReturn(true);
        when(user.getId()).thenReturn(1L);
        when(user.getPerfil()).thenReturn(Perfis.Aluno);

        var jwtTokenValue = "jwt-token";
        var jwtEncoderResult = mock(org.springframework.security.oauth2.jwt.Jwt.class);
        when(jwtEncoderResult.getTokenValue()).thenReturn(jwtTokenValue);
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(jwtEncoderResult);

        // Act
        ResponseEntity<DTOLoginResponse> response = usuarioController.loginUser(loginRequest);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(jwtTokenValue, response.getBody().acessString());
        assertEquals(300L, response.getBody().expiresIn());
    }

    @Test
    void createdUser_shouldReturnOk_whenUserCreatedSuccessfully() {
        // Arrange
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "password", Perfis.Aluno);
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.createUsuario(dto)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.createdUser(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void createdUser_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "password", Perfis.Aluno);
        when(usuarioService.createUsuario(dto)).thenThrow(new RuntimeException("Erro ao criar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.createdUser(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void upgradeUsuario_shouldReturnOk_whenUserUpdatedSuccessfully() {
        // Arrange
        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", "test@example.com", "nova_senha");
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.atualizarUsuario(dto.email(), dto)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.upgradeUsuario(dto);

        // Assert
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void upgradeUsuario_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", "test@example.com", "nova_senha");
        when(usuarioService.atualizarUsuario(dto.email(), dto)).thenThrow(new RuntimeException("Erro ao atualizar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.upgradeUsuario(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void confirmarEmail_shouldReturnOk_whenEmailConfirmedSuccessfully() {
        // Arrange
        String token = "valid-token";
        EmailConfirmacaoToken emailToken = mock(EmailConfirmacaoToken.class);
        Usuario usuario = mock(Usuario.class);
        when(emailConfirmacaoTokenService.findByToken(token)).thenReturn(Optional.of(emailToken));
        when(emailToken.estaExpirado()).thenReturn(false);
        when(emailToken.getUsuario()).thenReturn(usuario);

        // Act
        ResponseEntity<String> response = usuarioController.confirmarEmail(token);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Email confirmado com sucesso!", response.getBody());
    }

    @Test
    void confirmarEmail_shouldReturnBadRequest_whenTokenIsInvalid() {
        // Arrange
        String token = "invalid-token";
        when(emailConfirmacaoTokenService.findByToken(token)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = usuarioController.confirmarEmail(token);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Token inválido", response.getBody());
    }

    @Test
    void confirmarEmail_shouldReturnBadRequest_whenTokenIsExpired() {
        // Arrange
        String token = "expired-token";
        EmailConfirmacaoToken emailToken = mock(EmailConfirmacaoToken.class);
        when(emailConfirmacaoTokenService.findByToken(token)).thenReturn(Optional.of(emailToken));
        when(emailToken.estaExpirado()).thenReturn(true);

        // Act
        ResponseEntity<String> response = usuarioController.confirmarEmail(token);

        // Assert
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Token expirado", response.getBody());
    }
   
}