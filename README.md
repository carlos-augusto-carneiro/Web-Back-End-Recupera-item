# Projeto Recupera

## Requisitos

- Java 17 ou superior
- Docker Desktop
- Maven
- Git

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone [URL_DO_REPOSITÓRIO]
cd recupera
```

2. Crie o arquivo `.env` na raiz do projeto com as seguintes variáveis:
```env
# Configurações do PostgreSQL
POSTGRES_DB=recupera
POSTGRES_USER=postgres (Pode adicionar seu proprio usuario)
POSTGRES_PASSWORD=postgres (Pode adicioanr sua propria senha)

# Configurações da Aplicação Spring Boot
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/recupera
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Configurações do Servidor
SERVER_PORT=8080

# Configurações do Swagger
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
```

## Executando com Docker

### Comandos Docker

1. Construir e iniciar os containers:
```bash
docker-compose up --build
```

2. Executar em background:
```bash
docker-compose up -d
```

3. Parar os containers:
```bash
docker-compose down
```

4. Visualizar logs:
```bash
docker-compose logs -f
```

5. Reconstruir um serviço específico:
```bash
docker-compose up -d --build [nome_do_servico]
```

6. Reconstruir apenas o container da aplicação:
```bash
docker-compose up -d --build app
```

### Portas e Endpoints

- Aplicação Spring Boot: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- PostgreSQL: `localhost:5432`

### Estrutura dos Containers

- **recupera-app**: Aplicação Spring Boot
  - Porta: 8080
  - Dependências: PostgreSQL

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
docker-compose ps

# Verifique os logs do PostgreSQL
docker-compose logs postgres
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
3. Execute o projeto com Maven:
```bash
./mvnw spring-boot:run
```

## Documentação da API

A documentação da API está disponível através do Swagger UI:
- URL: `http://localhost:8080/swagger-ui.html`
