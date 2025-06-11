package com.recupera.item.back.recupera.domain.model.usuario;

import java.util.EnumSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.recupera.item.back.recupera.domain.dto.usuario.DTOCreatedUsuario;
import com.recupera.item.back.recupera.domain.dto.usuario.DTOLoginRequest;
import com.recupera.item.back.recupera.domain.enums.Perfis;
import com.recupera.item.back.recupera.domain.exception.usuario.SenhaFracaException;
import com.recupera.item.back.recupera.domain.exception.usuario.UsuarioException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Size(min = 3, max = 100, message = "Nome deve ter no mínimo 3 caracteres e no máximo 100 caracteres")
    public String nome;

    @Email(message="Email invalido")
    public String email;

    public String senha;

    @Enumerated(EnumType.STRING)
    public Perfis perfil;
    
    private static final Set<Perfis> PERFIS_PERMITIDOS = EnumSet.of(Perfis.Aluno, Perfis.Professor, Perfis.Guarda, Perfis.Administrador);


    public Usuario() {
    }

    public Usuario(Long id, String nome, String email, String senha, Perfis perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.perfil = perfil != null ? perfil : Perfis.Aluno;
    }

    public static Usuario fromDTO(DTOCreatedUsuario dto) {
        return new Usuario(null, dto.nome(), dto.email(), dto.senha(), dto.perfil());
    }

    public void promover(Perfis novoPerfil) {
        if (novoPerfil == null || !PERFIS_PERMITIDOS.contains(novoPerfil)) {
            throw new UsuarioException("Perfil inválido para promoção");
        }
        this.perfil = novoPerfil;
    }

    public void Validar(){
        if (nome == null || nome.isEmpty()) {
            throw new UsuarioException("Nome não pode ser vazio");
        }
        if (email == null || email.isEmpty()) {
            throw new UsuarioException("Email não pode ser vazio");
        }
        if (senha == null || senha.isEmpty()) {
            throw new UsuarioException("Senha não pode ser vazia");
        }
        if (perfil == null) {
            throw new UsuarioException("Perfil não pode ser nulo");
        }
    }

    public boolean validarSenha(String senha){
        boolean temMaiuscula = senha.matches(".*[A-Z].*");
        boolean temMinuscula = senha.matches(".*[a-z].*");
        boolean temNumero = senha.matches(".*[0-9].*");
        boolean temCaracterEspecial = senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        boolean tamanhoValido = senha.length() >= 8 && senha.length() <= 20;
        return temMaiuscula && temMinuscula && temNumero && temCaracterEspecial && tamanhoValido;
    }

    public String Senhaforte(String senha) {
        if (!validarSenha(senha)) {
            throw new UsuarioException("Senha fraca");
        }
        return "Senha forte";
    }

    public boolean validarEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public String EmailValido(String email) {
        if (validarEmail(email)) {
            return "Email válido";
        } else {
            return "Email inválido. Deve seguir o formato: @exemplo.com";
        }
    }

    public boolean autenticar(String senhadigitada) {
        if (this.senha.equals(senhadigitada)) {
            return true;
        } else {
            throw new IllegalArgumentException("Senha incorreta");
        }
    }

    public boolean autenticar(String senhaDigitada, PasswordEncoder encoder) {
        return encoder.matches(senhaDigitada, this.senha);
    }
    public boolean LoginCorrect(DTOLoginRequest loginDto, PasswordEncoder passwordEncoder) {
        if (loginDto == null || loginDto.senha() == null) {
            return false;
        }
        return passwordEncoder.matches(loginDto.senha(), this.senha);
    }

    public boolean ehAluno(){
        return this.perfil == Perfis.Aluno;
    }
    public boolean ehProfessor(){
        return this.perfil == Perfis.Professor;
    }
    public boolean ehGuarda(){
        return this.perfil == Perfis.Guarda;
    }

    public boolean ehAdministrador(){
        return this.perfil == Perfis.Administrador;
    }

    public void alterarSenha(String senha) {
        if (!validarSenha(senha)) {
            throw new SenhaFracaException("Senha fraca. Deve conter pelo menos uma letra maiúscula, um caractere especial e ter no mínimo 8 caracteres.");
        }
        this.senha = senha;    
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Perfis getPerfil() {
        return perfil;
    }
    public void setPerfil(Perfis perfil) {
        this.perfil = perfil;
    }


}
