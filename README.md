
# WalletPlus

## Arquitetura da Solução

![Arquitetura](docs/architecture.png)

A solução é baseada em microsserviços, onde cada serviço é responsável por uma funcionalidade específica. A comunicação entre os serviços é feita via APIs REST. A persistência dos dados é feita em um banco de dados PostgreSQL.

## Endpoints

### Transações
- `POST /api/wallet/transaction/add`: Adiciona uma nova transação.
- `POST /api/wallet/transaction/withdraw`: Saca uma transação.
- `POST /api/wallet/transaction/purchase`: Compra uma transação.
- `POST /api/wallet/transaction/cancel`: Cancela uma transação.
- `POST /api/wallet/transaction/refund`: Reembolsa uma transação.
- `GET /api/wallet/transactions`: Consulta todas as transações.
- `GET /api/wallet/transactions/{userId}`: Consulta o extrato de transações por usuário.

### Saldo
- `GET /api/wallet/balance/{userId}`: Consulta o saldo atual de um usuário.

### Usuários
- `POST /api/wallet/user`: Cria um novo usuário.
- `GET /api/wallet/user/{id}`: Consulta um usuário por ID.
- `GET /api/wallet/users`: Consulta todos os usuários.
- `PUT /api/wallet/user/{id}`: Atualiza um usuário por ID.
- `DELETE /api/wallet/user/{id}`: Deleta um usuário por ID.

## Configuração do Banco de Dados

A aplicação utiliza PostgreSQL como banco de dados. Certifique-se de que o PostgreSQL está instalado e configurado corretamente. As configurações de conexão com o banco de dados podem ser ajustadas no arquivo `application.yml`.

### Exemplo de `application.yml`

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/walletplus
    username: seu_usuario
    password: sua_senha
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

## Docker

Para rodar a aplicação em um container Docker, execute:

```sh
docker-compose up --build
```

### Exemplo de `docker-compose.yml`

```yaml
version: '3.8'
services:
  postgres:
    image: postgres:latest
    environment:
      POSTGRES_DB: walletplus
      POSTGRES_USER: seu_usuario
      POSTGRES_PASSWORD: sua_senha
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres

volumes:
  postgres_data:
```

## Swagger

Para acessar o Swagger da aplicação, acesse o seguinte endereço:

```
http://localhost:8080/swagger-ui/index.html
```

## Testes

Para rodar os testes da aplicação, execute:

```sh
./mvnw test -Denv=test
```

ou

```sh
mvn test -Denv=test
```


## Contribuição

Para contribuir com o projeto, siga os passos abaixo:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`).
4. Faça o push para a branch (`git push origin feature/nova-feature`).
5. Abra um Pull Request.

## Licença

Este projeto está licenciado sob os termos da licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
```