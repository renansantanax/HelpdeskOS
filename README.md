# HelpdeskOS

Uma aplicação de sistema de Help Desk composta por um **back-end** em Java com Spring Boot e um **front-end** em Angular, orquestrados via Docker Compose.

---

## 🔍 Visão Geral

O **HelpdeskOS** é uma plataforma para gerenciamento de chamados (tickets) que permite:
- Autenticação e autorização de usuários com **Spring Security** e **JWT**
- Criação, listagem, atualização e encerramento de chamados
- Envio e armazenamento de anexos aos chamados
- Notificações assíncronas por **RabbitMQ** e envio de e-mails via **Mailhog**
- Dashboards customizados por perfil de usuário
- Interface em **Angular** com roteamento, guards e interceptors

---

## 🛠 Tecnologias

### Back-end
- **Java 21** + **Spring Boot 3.4.4**
- **Spring Data JPA** (PostgreSQL) & **Spring Data MongoDB**
- **Spring Web MVC**, **Validation**
- **Spring Security** + **jjwt** para **JWT**
- **Spring AMQP** (RabbitMQ) + **Spring Mail**
- **ModelMapper** para mapeamento DTO⇄Entidade
- **Lombok** (`@Data`, `@Builder`, etc.)
- **SpringDoc OpenAPI** (Swagger UI)

### Front-end
- **Angular 19** (Standalone Components)
- **TypeScript**, **RxJS**, **HttpClientModule**
- **Angular Router**, **CanActivate Guards**
- **HTTP Interceptor** para injeção de token JWT
- **Pipes** customizados (`idMask`, `perfilFormat`)
- **ngx-toastr** + **BrowserAnimationsModule**

### Infraestrutura & DevOps
- **Docker** & **Docker Compose**
- **PostgreSQL** (container `postgres_osservice`)
- **RabbitMQ** (container `rabbitmq_osservice`)
- **Mailhog** (container `mailhog_osservice`)
- **NGINX** para servir o front-end
- Variáveis de ambiente para conexão a DB, MQ e SMTP

---

## 🏗 Arquitetura

### Back-end (camadas)
1. **Controllers** (`application/controllers`): expõem endpoints REST  
2. **Serviços de Domínio** (`domain/services`): regras de negócio e validações  
3. **DTOs & Mappers** (`domain/dtos` + `domain/mappers`): padronizam payloads  
4. **Entidades JPA** (`domain/entities`) e **Repositórios** (Spring Data)  
5. **Infraestrutura** (`infrastructure`): configuração (CORS, segurança, ModelMapper), componentes de mensageria, armazenamento local de arquivos  
6. **Handlers Globais**: tratamento centralizado de exceções e validações  

### Front-end (modular)
- **configurations**: constantes de rota e endpoints  
- **guards**: proteção de rotas (AuthGuard, ReverseAuthGuard)  
- **interceptors**: injeção de token JWT nas requisições HTTP  
- **services**: lógica de autenticação e tema (dark/light)  
- **components/pages**: telas (login, dashboard, chamados, usuários)  
- **components/layout**: navbar e layout geral  
- **pipes**: formatação de IDs e perfis  
- **models**: definição de interfaces (ex: `User`)

---

## 🎯 Padrões e Princípios

### SOLID
- **S**ingle Responsibility  
- **O**pen/Closed  
- **L**iskov Substitution  
- **I**nterface Segregation  
- **D**ependency Inversion  

### Design Patterns
- **Repository** (Spring Data)  
- **Service** (camada de negócio)  
- **DTO** (transporte de dados)  
- **Builder** (Lombok `@Builder`)  
- **Factory** (beans de `RabbitTemplate`, `ModelMapper`, `PasswordEncoder`)  
- **Observer**/Listener (`@RabbitListener`)  
- **Strategy** (`AnexoStorage` abstrai estratégias de armazenamento)

---

## 🚀 Como Executar

### Pré-requisitos
- Docker & Docker Compose  
- NodeJS

### Com Docker Compose
```bash
docker-compose up --build -d
```

- **Back-end: http://localhost:8083
- **Front-end: http://localhost:4200
- **Swagger UI: http://localhost:8083/swagger-ui/index.html
- **Mailhog: http://localhost:8025

### 📄 Licença
Este projeto está licenciado sob a MIT License.
