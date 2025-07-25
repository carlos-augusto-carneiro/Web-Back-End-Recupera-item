# Diagrama de Classes - Sistema Recupera Item

```mermaid
classDiagram
    class Usuario {
        -Long id
        -String nome
        -String email
        -String senha
        -boolean emailConfirmado
        -Perfis perfil
        +fromDTO(DTOCreatedUsuario) Usuario
        +promover(Perfis) void
        +validar() void
        +autenticar(String) boolean
        +ehAluno() boolean
        +ehProfessor() boolean
        +ehGuarda() boolean
        +ehAdministrador() boolean
        +alterarSenha(String) void
        +LoginCorrect(DTOLoginRequest, PasswordEncoder) boolean
        +validarSenha(String) boolean
        +validarEmail(String) boolean
        +Senhaforte(String) String
        +EmailValido(String) String
    }

    class Item {
        -long id
        -Date dataCriacao
        -String nome
        -String descricao
        -Boolean devolvido
        -String caminhoImagem
        -Usuario usuario
        +Item()
        +Item(Usuario, String, String, String)
        +marcarComoDevolvido() void
        +getCaminhoImagem() String
        +setCaminhoImagem(String) void
        +getId() long
        +setId(long) void
        +getNome() String
        +setNome(String) void
        +getDescricao() String
        +setDescricao(String) void
        +isDevolvido() Boolean
        +setDevolvido(Boolean) void
        +getDataCriacao() Date
        +setDataCriacao(Date) void
        +getUsuario() Usuario
        +setUsuario(Usuario) void
    }

    class EmailConfirmacaoToken {
        -Long id
        -String token
        -LocalDateTime dataExpiracao
        -Usuario usuario
        +criarPara(Usuario) EmailConfirmacaoToken
        +estaExpirado() boolean
        +getId() Long
        +setId(Long) void
        +getToken() String
        +setToken(String) void
        +getDataExpiracao() LocalDateTime
        +setDataExpiracao(LocalDateTime) void
        +getUsuario() Usuario
        +setUsuario(Usuario) void
    }

    class TokenRecuperacaoSenha {
        -Long id
        -String token
        -LocalDateTime dataExpiracao
        -Usuario usuario
        +estaExpirado() boolean
        +pertenceAoUsuario(Usuario) boolean
        +getId() Long
        +setId(Long) void
        +getToken() String
        +setToken(String) void
        +getDataExpiracao() LocalDateTime
        +setDataExpiracao(LocalDateTime) void
        +getUsuario() Usuario
        +setUsuario(Usuario) void
    }

    class Perfis {
        <<enumeration>>
        Professor
        Aluno
        Guarda
        Administrador
    }

    class UsuarioController {
        -UsuarioService usuarioService
        -PasswordEncoder passwordEncoder
        -JwtEncoder jwtEncoder
        -EmailConfirmacaoTokenService emailConfirmacaoTokenService
        -TokenRecuperacaoSenhaService tokenRecuperacaoSenhaService
        +loginUser(DTOLoginRequest) ResponseEntity~DTOLoginResponse~
        +createdUser(DTOCreatedUsuario) ResponseEntity~Void~
        +upgradeUsuario(DTOUpgradeUsuario) ResponseEntity~Void~
        +confirmarEmail(String) ResponseEntity~String~
        +recuperarSenha(String) ResponseEntity~String~
        +redefinirSenha(String, String) ResponseEntity~String~
    }

    class ItemController {
        -ItemService itemService
        -GoogleDriveConfig googleDriveService
        +listarItensPorUsuario(long) ResponseEntity
        +listarItensNaoDevolvidos() ResponseEntity
        +listarItensDevolvidos() ResponseEntity
        +buscarItemPorId(long) ResponseEntity
        +buscarItensPorNome(String) ResponseEntity
        +adicionarItem(MultipartFile, String, Authentication) ResponseEntity
        +atualizarItem(long, CriarItemDto, Authentication) ResponseEntity
        +marcarItemComoDevolvido(long, Authentication) ResponseEntity
        +excluirItem(long, Authentication) ResponseEntity
    }

    class AdminController {
        -UsuarioService usuarioService
        +listarUsuarios() ResponseEntity
        +deletarUsuario(String) ResponseEntity~Void~
        +promoverGuarda(String) ResponseEntity~Void~
        +promoverProfessor(String) ResponseEntity~Void~
        +promoverAluno(String) ResponseEntity~Void~
        +promoverAdministrador(String) ResponseEntity~Void~
    }

    class UsuarioService {
        -IUsuarioRepository usuarioRepository
        -BCryptPasswordEncoder passwordEncoder
        +createUsuario(DTOCreatedUsuario) Usuario
        +listarUsuarios() List~Usuario~
        +buscarUsuarioPorEmail(String) Usuario
        +deletarUsuarioPorEmail(String) void
        +promoverParaGuarda(String) Usuario
        +promoverParaProfessor(String) Usuario
        +promoverParaAluno(String) Usuario
        +promoverParaAdministrador(String) Usuario
        +atualizarUsuario(String, DTOUpgradeUsuario) Usuario
        +save(Usuario) Usuario
    }

    class ItemService {
        -IItemRepository itemRepository
        -IUsuarioRepository usuarioRepository
        +listarItensPorUsuario(long) List~Item~
        +listarItensNaoDevolvidos() List~Item~
        +listarItensDevolvidos() List~Item~
        +adicionarItem(CriarItemDto, long, String, String) Item
        +marcarItemComoDevolvido(long, long, String) Item
        +excluirItem(long, long, String) void
        +buscarItemPorId(long) Item
        +buscarItensPorNome(String) List~Item~
        +atualizarItem(long, CriarItemDto, long, String) Item
    }

    class EmailConfirmacaoTokenService {
        -IEmailConfirmacaoTokenRepository tokenRepository
        -EmailService emailService
        +enviarConfirmacaoEmail(Usuario) void
        +confirmarEmail(String) boolean
        +criarToken(Usuario) EmailConfirmacaoToken
    }

    class TokenRecuperacaoSenhaService {
        -ITokenRecuperacaoSenha tokenRepository
        -EmailService emailService
        +enviarTokenRecuperacao(String) void
        +validarToken(String) boolean
        +redefinirSenha(String, String) void
    }

    class IUsuarioRepository {
        <<interface>>
        +findByEmail(String) Optional~Usuario~
        +existsByEmail(String) boolean
        +findAllByPerfil(Perfis) List~Usuario~
        +deleteByEmail(String) void
        +save(Usuario) Usuario
        +findAll() List~Usuario~
    }

    class IItemRepository {
        <<interface>>
        +findByUsuarioId(Long) List~Item~
        +findByDevolvido(boolean) List~Item~
        +findByUsuarioIdAndDevolvido(Long, boolean) List~Item~
        +findByNomeContainingIgnoreCase(String) List~Item~
        +findById(Long) Optional~Item~
        +save(Item) Item
        +deleteById(Long) void
    }

    class IEmailConfirmacaoTokenRepository {
        <<interface>>
        +findByToken(String) Optional~EmailConfirmacaoToken~
        +findByUsuario(Usuario) Optional~EmailConfirmacaoToken~
        +save(EmailConfirmacaoToken) EmailConfirmacaoToken
        +delete(EmailConfirmacaoToken) void
    }

    class ITokenRecuperacaoSenha {
        <<interface>>
        +findByToken(String) Optional~TokenRecuperacaoSenha~
        +findByUsuario(Usuario) List~TokenRecuperacaoSenha~
        +save(TokenRecuperacaoSenha) TokenRecuperacaoSenha
        +deleteByUsuario(Usuario) void
    }

    %% Relacionamentos entre Entidades
    Usuario ||--o{ Item : possui
    Usuario ||--o| EmailConfirmacaoToken : tem
    Usuario ||--o{ TokenRecuperacaoSenha : tem
    Usuario }o--|| Perfis : tem

    %% Relacionamentos Controllers -> Services
    UsuarioController --> UsuarioService : usa
    UsuarioController --> EmailConfirmacaoTokenService : usa
    UsuarioController --> TokenRecuperacaoSenhaService : usa
    ItemController --> ItemService : usa
    AdminController --> UsuarioService : usa

    %% Relacionamentos Services -> Repositories
    UsuarioService --> IUsuarioRepository : usa
    ItemService --> IItemRepository : usa
    ItemService --> IUsuarioRepository : usa
    EmailConfirmacaoTokenService --> IEmailConfirmacaoTokenRepository : usa
    TokenRecuperacaoSenhaService --> ITokenRecuperacaoSenha : usa

    %% Relacionamentos Repositories -> Entidades
    IUsuarioRepository --> Usuario : gerencia
    IItemRepository --> Item : gerencia
    IEmailConfirmacaoTokenRepository --> EmailConfirmacaoToken : gerencia
    ITokenRecuperacaoSenha --> TokenRecuperacaoSenha : gerencia
```

## Descrição do Diagrama de Classes

### Entidades Principais

#### Usuario
- **Responsabilidade**: Representa os usuários do sistema
- **Atributos**: id, nome, email, senha, emailConfirmado, perfil
- **Métodos**: Validação, autenticação, promoção de perfil
- **Perfis**: Aluno, Professor, Guarda, Administrador

#### Item
- **Responsabilidade**: Representa itens perdidos/encontrados
- **Atributos**: id, dataCriacao, nome, descricao, devolvido, caminhoImagem, usuario
- **Métodos**: Marcar como devolvido, validações

#### EmailConfirmacaoToken
- **Responsabilidade**: Token para confirmação de email
- **Atributos**: id, token, dataExpiracao, usuario
- **Métodos**: Verificar expiração, criar token

#### TokenRecuperacaoSenha
- **Responsabilidade**: Token para recuperação de senha
- **Atributos**: id, token, dataExpiracao, usuario
- **Métodos**: Verificar expiração, validar propriedade

### Camadas da Arquitetura

#### Controllers (Apresentação)
- **UsuarioController**: Endpoints para gestão de usuários
- **ItemController**: Endpoints para gestão de itens
- **AdminController**: Endpoints administrativos

#### Services (Negócio)
- **UsuarioService**: Lógica de negócio para usuários
- **ItemService**: Lógica de negócio para itens
- **EmailConfirmacaoTokenService**: Gestão de tokens de confirmação
- **TokenRecuperacaoSenhaService**: Gestão de recuperação de senha

#### Repositories (Dados)
- **IUsuarioRepository**: Acesso a dados de usuários
- **IItemRepository**: Acesso a dados de itens
- **IEmailConfirmacaoTokenRepository**: Acesso a tokens de confirmação
- **ITokenRecuperacaoSenha**: Acesso a tokens de recuperação

### Padrões Utilizados
- **MVC (Model-View-Controller)**
- **Repository Pattern**
- **Service Layer Pattern**
- **Dependency Injection**
- **Data Transfer Object (DTO)**
