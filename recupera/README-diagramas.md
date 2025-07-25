# Documentação dos Diagramas - Sistema Recupera Item

Este diretório contém a documentação completa da arquitetura do Sistema Recupera Item, organizada em diagramas específicos para diferentes aspectos do sistema.

## 📁 Arquivos de Diagramas

### 🏗️ [Diagrama C4](./diagrama-c4.md)
Documentação da arquitetura do sistema em três níveis:
- **Contexto**: Visão geral e interações externas
- **Container**: Componentes principais do sistema
- **Componente**: Estrutura interna da API REST

### 🎯 [Diagrama de Classes](./diagrama-classes.md)
Modelagem orientada a objetos completa:
- **Entidades**: Usuario, Item, EmailConfirmacaoToken, TokenRecuperacaoSenha
- **Controllers**: Camada de apresentação (REST API)
- **Services**: Lógica de negócio
- **Repositories**: Camada de acesso a dados

### 🗄️ [Diagrama ER](./diagrama-er.md)
Estrutura do banco de dados:
- **Tabelas**: Definição completa com tipos e restrições
- **Relacionamentos**: Cardinalidade e integridade referencial
- **Índices**: Otimização de consultas
- **Regras de Negócio**: Constraints e validações

## 🚀 Visão Geral do Sistema

### Funcionalidades Principais
1. **👥 Gestão de Usuários**
   - Cadastro e autenticação
   - 4 perfis: Aluno, Professor, Guarda, Administrador
   - Confirmação de email e recuperação de senha

2. **📦 Gestão de Itens**
   - Cadastro de itens perdidos/encontrados
   - Upload de imagens via Google Drive
   - Controle de status (devolvido/não devolvido)

3. **🔐 Sistema de Autenticação**
   - JWT tokens para sessões
   - Tokens de confirmação de email
   - Tokens de recuperação de senha

4. **🛡️ Controle de Acesso**
   - Autorização baseada em perfis
   - Endpoints protegidos por roles
   - Operações administrativas restritas

5. **🔗 Integrações Externas**
   - Google Drive API para armazenamento
   - Serviços de email (SMTP)

### Tecnologias Utilizadas
- **Backend**: Spring Boot, Spring Security, Spring Data JPA
- **Banco de Dados**: PostgreSQL/MySQL
- **Autenticação**: JWT (JSON Web Tokens)
- **Cloud Storage**: Google Drive API
- **Documentação API**: Swagger/OpenAPI

### Padrões Arquiteturais
- **MVC (Model-View-Controller)**
- **Repository Pattern**
- **Service Layer Pattern**
- **Dependency Injection**
- **RESTful API Design**

## 📊 Como Visualizar os Diagramas

Os diagramas estão em formato **Mermaid** e podem ser visualizados em:

### Editores com Suporte Nativo
- **GitHub/GitLab**: Renderização automática
- **VS Code**: Com extensão Mermaid Preview
- **IntelliJ IDEA**: Com plugin Mermaid

### Ferramentas Online
- [Mermaid Live Editor](https://mermaid.live/)
- [Mermaid Chart](https://www.mermaidchart.com/)

### Extensões Recomendadas (VS Code)
```
- Mermaid Markdown Syntax Highlighting
- Markdown Preview Mermaid Support
- Mermaid Preview
```

## 🔄 Fluxos Principais

### Cadastro de Usuário
1. Usuário preenche formulário de cadastro
2. Sistema valida dados e cria usuário (perfil: Aluno)
3. Token de confirmação é enviado por email
4. Usuário confirma email através do link

### Cadastro de Item
1. Usuário autorizado (Guarda/Professor/Admin) faz login
2. Preenche dados do item + upload de imagem
3. Sistema salva item e faz upload para Google Drive
4. Item fica disponível para busca

### Devolução de Item
1. Usuário autorizado localiza item na lista
2. Marca item como devolvido
3. Sistema atualiza status no banco de dados

## 📈 Métricas e Monitoramento

### Possíveis Melhorias Futuras
- Dashboard administrativo
- Relatórios de itens mais perdidos
- Notificações push
- Sistema de categorias
- Chat integrado
- API para aplicativo mobile

## 🤝 Contribuição

Para contribuir com a documentação:
1. Mantenha a consistência dos diagramas
2. Atualize todos os diagramas relacionados quando houver mudanças
3. Documente novas funcionalidades
4. Valide os diagramas Mermaid antes do commit

---

**Sistema Recupera Item** - Gestão Inteligente de Achados e Perdidos 🎯
