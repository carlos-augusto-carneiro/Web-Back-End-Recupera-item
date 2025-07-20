package com.recupera.item.back.recupera.domain.model.usuario;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.exception.usuario.SenhaFracaException;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;



public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario(1L, "Carlos", "carlos@email.com", "Senha@123", Perfis.Aluno);
    }

    @Test
    void testValidarComDadosValidosNaoLancaExcecao() {
        assertDoesNotThrow(() -> usuario.Validar());
    }

    @Test
    void testValidarNomeVazioLancaExcecao() {
        usuario.setNome("");
        UsuarioException ex = assertThrows(UsuarioException.class, () -> usuario.Validar());
        assertEquals("Nome não pode ser vazio", ex.getMessage());
    }

    @Test
    void testValidarEmailVazioLancaExcecao() {
        usuario.setEmail("");
        UsuarioException ex = assertThrows(UsuarioException.class, () -> usuario.Validar());
        assertEquals("Email não pode ser vazio", ex.getMessage());
    }

    @Test
    void testValidarSenhaVaziaLancaExcecao() {
        usuario.setSenha("");
        UsuarioException ex = assertThrows(UsuarioException.class, () -> usuario.Validar());
        assertEquals("Senha não pode ser vazia", ex.getMessage());
    }

    @Test
    void testValidarPerfilNuloLancaExcecao() {
        usuario.setPerfil(null);
        UsuarioException ex = assertThrows(UsuarioException.class, () -> usuario.Validar());
        assertEquals("Perfil não pode ser nulo", ex.getMessage());
    }

    @Test
    void testValidarSenhaForte() {
        assertTrue(usuario.validarSenha("Senha@123"));
        assertFalse(usuario.validarSenha("senha123"));
        assertFalse(usuario.validarSenha("SENHA123"));
        assertFalse(usuario.validarSenha("Senha123"));
        assertFalse(usuario.validarSenha("Sen@1"));
    }

    @Test
    void testSenhaforteMensagem() {
        assertEquals("Senha forte", usuario.Senhaforte("Senha@123"));
        UsuarioException ex = assertThrows(UsuarioException.class, () -> usuario.Senhaforte("senha123"));
        assertTrue(ex.getMessage().startsWith("Senha fraca"));
    }

    @Test
    void testValidarEmailValido() {
        assertTrue(usuario.validarEmail("teste@exemplo.com"));
        assertFalse(usuario.validarEmail("teste@exemplo"));
        assertFalse(usuario.validarEmail("teste.com"));
    }

    @Test
    void testEmailValidoMensagem() {
        assertEquals("Email válido", usuario.EmailValido("teste@exemplo.com"));
        assertTrue(usuario.EmailValido("teste@exemplo").startsWith("Email inválido"));
    }

    @Test
    void testAutenticarSenhaCorreta() {
        usuario.setSenha("Senha@123");
        assertTrue(usuario.autenticar("Senha@123"));
    }

    @Test
    void testAutenticarSenhaIncorretaLancaExcecao() {
        usuario.setSenha("Senha@123");
        assertThrows(IllegalArgumentException.class, () -> usuario.autenticar("Errada@123"));
    }

    @Test
    void testAutenticarComPasswordEncoder() {
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        when(encoder.matches("Senha@123", "Senha@123")).thenReturn(true);
        usuario.setSenha("Senha@123");
        assertTrue(usuario.autenticar("Senha@123", encoder));
    }

    @Test
    void testEhAlunoProfessorGuarda() {
        usuario.setPerfil(Perfis.Aluno);
        assertTrue(usuario.ehAluno());
        assertFalse(usuario.ehProfessor());
        assertFalse(usuario.ehGuarda());

        usuario.setPerfil(Perfis.Professor);
        assertTrue(usuario.ehProfessor());
        assertFalse(usuario.ehAluno());
        assertFalse(usuario.ehGuarda());

        usuario.setPerfil(Perfis.Guarda);
        assertTrue(usuario.ehGuarda());
        assertFalse(usuario.ehAluno());
        assertFalse(usuario.ehProfessor());
    }

    @Test
    void testPromoverParaAdministrador() {
        usuario.setPerfil(Perfis.Aluno);
        assertDoesNotThrow(() -> usuario.promover(Perfis.Administrador));
        assertEquals(Perfis.Administrador, usuario.getPerfil());
    }

    @Test
    void testEhAdministrador() {
        usuario.setPerfil(Perfis.Administrador);
        assertTrue(usuario.ehAdministrador());
        assertFalse(usuario.ehAluno());
        assertFalse(usuario.ehProfessor());
        assertFalse(usuario.ehGuarda());
    }

    @Test
    void testAlterarSenhaComSenhaForte() {
        usuario.alterarSenha("NovaSenha@123");
        assertEquals("NovaSenha@123", usuario.getSenha());
    }

    @Test
    void testAlterarSenhaComSenhaFracaLancaExcecao() {
        assertThrows(SenhaFracaException.class, () -> usuario.alterarSenha("fraca"));
    }

    @Test
    void testGettersAndSetters() {
        usuario.setId(2L);
        usuario.setNome("Novo Nome");
        usuario.setEmail("novo@email.com");
        usuario.setSenha("OutraSenha@123");
        usuario.setPerfil(Perfis.Professor);

        assertEquals(2L, usuario.getId());
        assertEquals("Novo Nome", usuario.getNome());
        assertEquals("novo@email.com", usuario.getEmail());
        assertEquals("OutraSenha@123", usuario.getSenha());
        assertEquals(Perfis.Professor, usuario.getPerfil());
    }
}