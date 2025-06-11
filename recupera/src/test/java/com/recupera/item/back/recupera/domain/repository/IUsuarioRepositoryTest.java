package com.recupera.item.back.recupera.domain.repository;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;


@DataJpaTest
class IUsuarioRepositoryTest {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = new Usuario();
        usuario.setNome("Carlos");
        usuario.setEmail("carlos@email.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(Perfis.Aluno);
        usuarioRepository.save(usuario);
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindByEmail() {
        Optional<Usuario> found = usuarioRepository.findByEmail("carlos@email.com");
        assertThat(found).isPresent();
        assertThat(found.get().getNome()).isEqualTo("Carlos");
    }

    @Test
    @DisplayName("Should check existence by email")
    void testExistsByEmail() {
        boolean exists = usuarioRepository.existsByEmail("carlos@email.com");
        assertThat(exists).isTrue();

        boolean notExists = usuarioRepository.existsByEmail("notfound@email.com");
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("Should find all users by perfil")
    void testFindAllByPerfil() {
        List<Usuario> aluno = usuarioRepository.findAllByPerfil(Perfis.Aluno);
        assertThat(aluno).extracting(Usuario::getEmail).contains("carlos@email.com");
    }

    @Test
    @DisplayName("Should delete user by email")
    void testDeleteByEmail() {
        usuarioRepository.deleteByEmail("carlos@email.com");
        Optional<Usuario> found = usuarioRepository.findByEmail("carlos@email.com");
        assertThat(found).isNotPresent();
    }
}