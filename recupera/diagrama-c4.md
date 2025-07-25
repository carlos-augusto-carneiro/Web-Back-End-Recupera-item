# Diagrama C4 - Sistema Recupera Item

## 1. Diagrama C4 - Nível de Contexto

```mermaid
C4Context
    title Diagrama de Contexto - Sistema Recupera Item

    Person(usuario, "Usuário", "Aluno, Professor, Guarda ou Administrador")
    System(recuperaSystem, "Sistema Recupera Item", "Sistema para gerenciar itens perdidos e encontrados")
    System_Ext(googleDrive, "Google Drive API", "Armazenamento de imagens dos itens")
    System_Ext(emailService, "Serviço de Email", "Envio de emails de confirmação e recuperação")
    System_Ext(database, "Banco de Dados", "PostgreSQL/MySQL para persistência")

    Rel(usuario, recuperaSystem, "Gerencia itens perdidos/encontrados")
    Rel(recuperaSystem, googleDrive, "Armazena imagens", "REST API")
    Rel(recuperaSystem, emailService, "Envia notificações", "SMTP")
    Rel(recuperaSystem, database, "Persiste dados", "JPA/SQL")
```

## 2. Diagrama C4 - Nível de Container

```mermaid
C4Container
    title Diagrama de Container - Sistema Recupera Item

    Person(usuario, "Usuário", "Usuários do sistema")

    Container_Boundary(recuperaSystem, "Sistema Recupera Item") {
        Container(frontend, "Frontend Web", "React/Angular", "Interface web para usuários")
        Container(api, "API REST", "Spring Boot", "Fornece funcionalidades via REST API")
        Container(database, "Banco de Dados", "PostgreSQL/MySQL", "Armazena usuários, itens e tokens")
    }

    System_Ext(googleDrive, "Google Drive API", "Armazenamento de imagens")
    System_Ext(emailService, "Serviço de Email", "SMTP")

    Rel(usuario, frontend, "Usa", "HTTPS")
    Rel(frontend, api, "Consome API", "HTTPS/REST")
    Rel(api, database, "Lê/Escreve", "JPA/SQL")
    Rel(api, googleDrive, "Upload imagens", "REST API")
    Rel(api, emailService, "Envia emails", "SMTP")
```

## 3. Diagrama C4 - Nível de Componente

```mermaid
C4Component
    title Diagrama de Componente - API REST

    Container_Boundary(api, "API REST - Spring Boot") {
        Component(userController, "User Controller", "Spring MVC", "Gerencia operações de usuários")
        Component(itemController, "Item Controller", "Spring MVC", "Gerencia operações de itens")
        Component(adminController, "Admin Controller", "Spring MVC", "Operações administrativas")
        
        Component(userService, "User Service", "Spring Service", "Lógica de negócio de usuários")
        Component(itemService, "Item Service", "Spring Service", "Lógica de negócio de itens")
        Component(emailService, "Email Service", "Spring Service", "Serviços de email")
        Component(tokenService, "Token Service", "Spring Service", "Gerencia tokens JWT e recuperação")
        
        Component(userRepository, "User Repository", "Spring Data JPA", "Acesso a dados de usuários")
        Component(itemRepository, "Item Repository", "Spring Data JPA", "Acesso a dados de itens")
        Component(tokenRepository, "Token Repository", "Spring Data JPA", "Acesso a tokens")
        
        Component(security, "Security Config", "Spring Security", "Autenticação e autorização")
        Component(googleDriveConfig, "Google Drive Config", "Google API", "Integração com Google Drive")
    }

    System_Ext(database, "Banco de Dados")
    System_Ext(googleDrive, "Google Drive API")
    System_Ext(emailExternal, "Serviço Email Externo")

    Rel(userController, userService, "Usa")
    Rel(itemController, itemService, "Usa")
    Rel(adminController, userService, "Usa")
    
    Rel(userService, userRepository, "Usa")
    Rel(itemService, itemRepository, "Usa")
    Rel(itemService, userRepository, "Usa")
    Rel(tokenService, tokenRepository, "Usa")
    
    Rel(userRepository, database, "Lê/Escreve")
    Rel(itemRepository, database, "Lê/Escreve")
    Rel(tokenRepository, database, "Lê/Escreve")
    
    Rel(itemService, googleDriveConfig, "Upload imagens")
    Rel(googleDriveConfig, googleDrive, "API calls")
    
    Rel(emailService, emailExternal, "Envia emails")
    
    Rel(security, userService, "Autentica")
```

## Descrição do Diagrama C4

### Nível de Contexto
Mostra o sistema em alto nível e suas interações com usuários e sistemas externos:
- **Usuários**: Alunos, Professores, Guardas e Administradores
- **Sistemas Externos**: Google Drive, Serviço de Email, Banco de Dados

### Nível de Container
Detalha os principais componentes do sistema:
- **Frontend Web**: Interface do usuário
- **API REST**: Backend em Spring Boot
- **Banco de Dados**: Persistência de dados

### Nível de Componente
Mostra a estrutura interna da API REST:
- **Controllers**: Pontos de entrada da API
- **Services**: Lógica de negócio
- **Repositories**: Acesso a dados
- **Configurações**: Segurança e integrações externas

### Benefícios da Arquitetura:
- **Separação de responsabilidades**
- **Escalabilidade horizontal**
- **Facilidade de manutenção**
- **Integração com serviços externos**
- **Segurança robusta com JWT**
