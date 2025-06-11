package com.recupera.item.back.recupera.controllers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginResponse;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.service.UsuarioService;

class UsuarioControllerTest {

    private UsuarioService usuarioService;
    private PasswordEncoder passwordEncoder;
    private JwtEncoder jwtEncoder;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtEncoder = mock(JwtEncoder.class);
        usuarioController = new UsuarioController(usuarioService, passwordEncoder, jwtEncoder);
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
    void loginUser_shouldThrowBadCredentialsException_whenUserNotFound() {
        // Arrange
        String email = "notfound@example.com";
        DTOLoginRequest loginRequest = new DTOLoginRequest(email, "password");
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(null);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> usuarioController.loginUser(loginRequest));
    }

    @Test
    void loginUser_shouldThrowBadCredentialsException_whenPasswordIncorrect() {
        // Arrange
        String email = "test@example.com";
        DTOLoginRequest loginRequest = new DTOLoginRequest(email, "wrongpassword");
        Usuario user = mock(Usuario.class);
        when(usuarioService.buscarUsuarioPorEmail(email)).thenReturn(user);
        when(user.LoginCorrect(eq(loginRequest), eq(passwordEncoder))).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> usuarioController.loginUser(loginRequest));
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
        verify(usuarioService).createUsuario(dto);
    }

    @Test
    void createdUser_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "password", Perfis.Aluno);
        when(usuarioService.createUsuario(dto)).thenThrow(new UsuarioException("Erro ao criar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.createdUser(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void listarUsuarios_shouldReturnListOfUsers() {
        // Arrange
        List<Usuario> usuarios = List.of(mock(Usuario.class), mock(Usuario.class));
        when(usuarioService.listarUsuarios()).thenReturn(usuarios);

        // Act
        ResponseEntity<?> response = usuarioController.listarUsuarios();

        // Assert
        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuarios, response.getBody());
    }

    @Test
    void listarUsuarios_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        when(usuarioService.listarUsuarios()).thenThrow(new UsuarioException("Erro ao listar usuários"));

        // Act
        ResponseEntity<?> response = usuarioController.listarUsuarios();

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void deletarUsuario_shouldReturnOk_whenUserDeletedSuccessfully() {
        // Arrange
        String email = "test@email.com";

        // Act
        ResponseEntity<Void> response = usuarioController.deletarUsuario(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(usuarioService).deletarUsuarioPorEmail(email);
    }

    @Test
    void deletarUsuario_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        String email = "test@email.com";
        doThrow(new UsuarioException("Erro ao deletar usuário")).when(usuarioService).deletarUsuarioPorEmail(email);

        // Act
        ResponseEntity<Void> response = usuarioController.deletarUsuario(email);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void promoverGuarda_shouldReturnOk_whenUserPromotedSuccessfully() {
        // Arrange
        String email = "test@email.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.promoverParaGuarda(email)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.promoverGuarda(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(usuarioService).promoverParaGuarda(email);
    }

    @Test
    void promoverProfessor_shouldReturnOk_whenUserPromotedSuccessfully() {
        // Arrange
        String email = "test@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.promoverParaProfessor(email)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.promoverProfessor(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(usuarioService).promoverParaProfessor(email);
    }

    @Test
    void promoverAluno_shouldReturnOk_whenUserPromotedSuccessfully() {
        // Arrange
        String email = "test@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.promoverParaAluno(email)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.promoverAluno(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(usuarioService).promoverParaAluno(email);
    }

    @Test
    void promoverAdministrador_shouldReturnOk_whenUserPromotedSuccessfully() {
        // Arrange
        String email = "test@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioService.promoverParaAdministrador(email)).thenReturn(usuario);

        // Act
        ResponseEntity<Void> response = usuarioController.promoverAdministrador(email);

        // Assert
        assertEquals(200, response.getStatusCode().value());
        verify(usuarioService).promoverParaAdministrador(email);
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
        verify(usuarioService).atualizarUsuario(dto.email(), dto);
    }

    @Test
    void upgradeUsuario_shouldReturnBadRequest_whenExceptionOccurs() {
        // Arrange
        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", "test@example.com", "nova_senha");
        when(usuarioService.atualizarUsuario(dto.email(), dto)).thenThrow(new UsuarioException("Erro ao atualizar usuário"));

        // Act
        ResponseEntity<Void> response = usuarioController.upgradeUsuario(dto);

        // Assert
        assertEquals(400, response.getStatusCode().value());
    }
}