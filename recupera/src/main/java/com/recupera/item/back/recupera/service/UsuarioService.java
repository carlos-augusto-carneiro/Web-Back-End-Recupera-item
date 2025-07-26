package com.recupera.item.back.recupera.service;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOUpgradeUsuario;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;
import com.recupera.item.back.recupera.domain.model.usuario.Usuario;
import com.recupera.item.back.recupera.domain.repository.IUsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired  
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(IUsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario createUsuario(DTOCreatedUsuario usuario) {
        if (usuario == null) {
            throw new UsuarioException("Dados do usuário não podem ser nulos");
        }
        Usuario usuarioConvert = Usuario.fromDTO(usuario);
        usuarioConvert.Validar();
        usuarioConvert.EmailValido(usuarioConvert.getEmail());
        usuarioConvert.Senhaforte(usuarioConvert.getSenha());

        if(usuarioRepository.existsByEmail(usuarioConvert.getEmail())) {
            throw new UsuarioException("Email já cadastrado");
        }

        String senhaCodificada = passwordEncoder.encode(usuarioConvert.getSenha());
        usuarioConvert.setSenha(senhaCodificada);

        return usuarioRepository.save(usuarioConvert);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }
    public Usuario buscarUsuarioPorEmail(String email) {
        
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
            return usuarioOpt.orElseThrow(() -> new UsuarioException("Usuário não encontrado com o email: " + email));
        } catch (Exception e) {
            System.out.println("DEBUG: Erro durante busca: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw e;
        }
    }
    public void deletarUsuarioPorEmail(String email) {
        if (!usuarioRepository.existsByEmail(email)) {
            throw new UsuarioException("Usuário não encontrado com o email: " + email);
        }
        Usuario usuario = buscarUsuarioPorEmail(email);
        if (usuario.getPerfil() == Perfis.Administrador) {
            throw new UsuarioException("Não é possível deletar um usuário com perfil de Administrador");
        }

        usuarioRepository.deleteById(usuario.getId());
    }

    public Usuario promoverParaGuarda(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Guarda);
        return usuarioRepository.save(usuario);
    }
    
    public Usuario promoverParaProfessor(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Professor);
        return usuarioRepository.save(usuario);
    }

    public Usuario promoverParaAluno(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Aluno);
        return usuarioRepository.save(usuario);
    }

    public Usuario promoverParaAdministrador(String email) {
        Usuario usuario = buscarUsuarioPorEmail(email);
        usuario.promover(Perfis.Administrador);
        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(String email, DTOUpgradeUsuario dto) {
        Usuario usuario = buscarUsuarioPorEmail(email);

        if (dto.nome() != null && !dto.nome().isEmpty()) {
            usuario.setNome(dto.nome());
        }
        if (dto.email() != null && !dto.email().isEmpty()) {
            if (usuarioRepository.existsByEmail(dto.email()) && !usuario.getEmail().equals(dto.email())) {
                throw new UsuarioException("Email já cadastrado");
            }
            usuario.setEmail(dto.email());
        }
        if (dto.senha() != null && !dto.senha().isEmpty()) {
            usuario.Senhaforte(dto.senha());
            String senhaCodificada = passwordEncoder.encode(dto.senha());
            usuario.setSenha(senhaCodificada);
        }

        usuario.Validar();
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
