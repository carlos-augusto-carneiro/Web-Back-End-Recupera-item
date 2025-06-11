package com.recupera.item.back.recupera.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.domain.repository.IUsuarioRepository;


class UsuarioServiceTest {

    private IUsuarioRepository usuarioRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        usuarioRepository = mock(IUsuarioRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    @Test
    void createUsuario_shouldCreateAndSaveUser() {
        DTOCreatedUsuario dto = new DTOCreatedUsuario("Test User", "test@email.com", "password", Perfis.Aluno);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.createUsuario(dto);

        assertEquals("Test User", result.getNome());
        assertEquals("test@email.com", result.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void createUsuario_shouldThrowExceptionIfUserIsNull() {
        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.createUsuario(null)
        );
        assertTrue(exception.getMessage().contains("Dados do usuário não podem ser nulos"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void listarUsuarios_shouldReturnListOfUsers() {
        Usuario usuario1 = mock(Usuario.class);
        Usuario usuario2 = mock(Usuario.class);
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario1, usuario2));

        List<Usuario> result = usuarioService.listarUsuarios();

        assertEquals(2, result.size());
        assertTrue(result.contains(usuario1));
        assertTrue(result.contains(usuario2));
    }

    @Test
    void buscarUsuarioPorEmail_shouldReturnUserIfFound() {
        String email = "email@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
    }

    @Test
    void deletarUsuarioPorEmail_shouldDeleteUserIfFound() {
        String email = "email@example.com";
        when(usuarioRepository.existsByEmail(email)).thenReturn(true);

        usuarioService.deletarUsuarioPorEmail(email);

        verify(usuarioRepository).deleteByEmail(email);
    }

    @Test
    void promoverParaAluno_shouldPromoteUserToAlunoAndSave() {
        String email = "test@email.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.promoverParaAluno(email);

        verify(usuario).promover(Perfis.Aluno);
        verify(usuarioRepository).save(usuario);
        assertEquals(usuario, result);
    }

    @Test
    void promoverParaAluno_shouldThrowExceptionIfUserNotFound() {
        String email = "notfound@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.promoverParaAluno(email)
        );
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void promoverParaGuarda_shouldPromoteUserToGuardaAndSave() {
        String email = "email@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.promoverParaGuarda(email);

        verify(usuario).promover(Perfis.Guarda);
        verify(usuarioRepository).save(usuario);
        assertEquals(usuario, result);
    }

    @Test
    void promoverParaGuarda_shouldThrowExceptionIfUserNotFound() {
        String email = "notfound@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.promoverParaGuarda(email)
        );
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void promoverParaProfessor_shouldPromoteUserToProfessorAndSave() {
        String email = "email@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.promoverParaProfessor(email);

        verify(usuario).promover(Perfis.Professor);
        verify(usuarioRepository).save(usuario);
        assertEquals(usuario, result);
    }

    @Test
    void promoverParaProfessor_shouldThrowExceptionIfUserNotFound() {
        String email = "notfound@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.promoverParaProfessor(email)
        );
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void promoverParaAdministrador_shouldPromoteUserToAdministradorAndSave() {
        String email = "email@example.com";
        Usuario usuario = mock(Usuario.class);
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario result = usuarioService.promoverParaAdministrador(email);

        verify(usuario).promover(Perfis.Administrador);
        verify(usuarioRepository).save(usuario);
        assertEquals(usuario, result);
    }

    @Test
    void promoverParaAdministrador_shouldThrowExceptionIfUserNotFound() {
        String email = "notfound@email.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsuarioException exception = assertThrows(
                UsuarioException.class,
                () -> usuarioService.promoverParaAdministrador(email)
        );
        assertTrue(exception.getMessage().contains("Usuário não encontrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void atualizarUsuario_shouldUpdateUserDetails() {
        String email = "email@example.com";
        Usuario usuario = new Usuario();
        usuario.setNome("Nome Antigo");
        usuario.setEmail(email);
        usuario.setSenha("senha_antiga");
        usuario.setPerfil(Perfis.Aluno);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(passwordEncoder.encode("nova_senha")).thenReturn("senha_codificada");

        DTOUpgradeUsuario dto = new DTOUpgradeUsuario("Novo Nome", email, "nova_senha");
        Usuario result = usuarioService.atualizarUsuario(email, dto);

        assertEquals("Novo Nome", result.getNome());
        assertEquals("senha_codificada", result.getSenha());
        verify(usuarioRepository).save(usuario);
    }

}