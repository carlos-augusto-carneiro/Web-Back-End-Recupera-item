# Projeto Recupera

## Atenção o salvar item

O salvar item vai precisar de uma key que não tem como subir no github.

## Requisitos

- Java 17 ou superior
- Docker Desktop
- Maven
- Git

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITORIO]
cd recupera
```

2. Crie o arquivo `.env` na raiz do diretório `recupera/` com as seguintes variáveis:
```env
# Banco de Dados
POSTGRES_DB=recupera
POSTGRES_USER=carlos
POSTGRES_PASSWORD=1234

SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/recupera
SPRING_DATASOURCE_USERNAME=${POSTGRES_USER}
SPRING_DATASOURCE_PASSWORD=${POSTGRES_PASSWORD}

# Aplicações
SERVER_PORT=8080
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html

# Email
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=perdidosachadosufc@gmail.com
SPRING_MAIL_PASSWORD=teaq oyab uxaj adin
SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
SPRING_MAIL_PROPERTIES_MAIL_DEBUG=false

```

## Executando com Docker

> **Atenção:** Todos os comandos abaixo devem ser executados dentro do diretório `recupera/`.

### Comandos Docker

1. Construir e iniciar os containers:
```bash
docker compose up --build
```

2. Executar em background:
```bash
docker compose up -d
```

3. Parar os containers:
```bash
docker compose down
```

4. Visualizar logs:
```bash
docker compose logs -f
```

5. Reconstruir um serviço específico:
```bash
docker compose up -d --build [nome_do_servico]
```

6. Reconstruir apenas o container da aplicação:
```bash
docker compose up -d --build app
```

### Portas e Endpoints

- Aplicação Spring Boot: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- PostgreSQL: `localhost:5432`

### Estrutura dos Containers

- **recupera-app**: Aplicação Spring Boot
  - Porta: 8080
  - Depende do serviço: recupera-postgres
  - Utiliza o script `wait-for-it.sh` para aguardar o banco de dados iniciar antes de subir a aplicação

- **recupera-postgres**: Banco de dados PostgreSQL
  - Porta: 5432
  - Volume: postgres_data (persistência dos dados)

## Solução de Problemas

### Docker não está rodando
```bash
# Verifique se o Docker Desktop está instalado e rodando
# Se necessário, reinicie o Docker Desktop
```

### Erro de conexão com o banco
```bash
# Verifique se o container do PostgreSQL está rodando
docker compose ps

# Verifique os logs do PostgreSQL
docker compose logs postgres
```

### Limpar ambiente Docker
```bash
# Remove todos os containers, imagens e volumes não utilizados
docker system prune -a
docker volume prune
```

## Desenvolvimento Local

Para executar o projeto sem Docker:

1. Instale o PostgreSQL localmente
2. Configure as variáveis de ambiente no seu sistema
3. Execute o projeto com Maven (dentro do diretório `recupera/`):
```bash
./mvnw spring-boot:run
```

## Documentação da API

A documentação da API está disponível através do Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`
