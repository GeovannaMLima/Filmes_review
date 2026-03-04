# Cinema API - Watchlist Manager

Uma API RESTful desenvolvida em **Java** com **Spring Boot** para gerenciamento de listas de filmes (Watchlist) de usuários. O sistema integra-se com a API pública do OMDB para busca de títulos e aplica conceitos de Engenharia de Software, como arquitetura limpa, resiliência e alta cobertura de testes da regra de negócio.

##  O Projeto
O objetivo desta aplicação é permitir que os usuários criem contas, autentiquem-se de forma segura e gerenciem suas listas de filmes assistidos ou para assistir, podendo adicionar avaliações (notas e reviews). A aplicação foi desenhada com foco em performance de banco de dados e tratamento rigoroso de exceções.

##  Objetivos de Aprendizado
Este projeto foi construído como um laboratório prático para consolidar conceitos avançados de desenvolvimento Back-end:
* **Segurança e Infraestrutura:** Implementação de JWT, Spring Security e proteção de rotas.
* **Integração de APIs Externas:** Consumo de serviços de terceiros utilizando OpenFeign.
* **Resiliência e Status HTTP:** Criação de um Global Exception Handler (`@RestControllerAdvice`) para tratamento padronizado de erros e retornos de Status Codes semânticos (400, 404, 409).
* **Garantia de Qualidade:** Blindagem de regras de negócio utilizando testes unitários com JUnit 5 e Mockito.

##  Tecnologias e Ferramentas
- **Java 17+**
- **Spring Boot 3** (Web, Data JPA, Security)
- **Spring Security & JWT** (JSON Web Tokens para autenticação)
- **Spring Cloud OpenFeign** (Consumo de API Externa)
- **Banco de Dados:** PostgreSQL 
- **Infraestrutura:** Docker (Containerização)
- **Testes:** JUnit 5 e Mockito
- **Validações e Tratamento:** Bean Validation e `@RestControllerAdvice`

##  Principais Funcionalidades e Regras de Negócio

### Gestão de Usuários e Segurança
- Cadastro de usuários com criptografia de senhas (BCrypt).
- Bloqueio de cadastros com e-mails (login) já existentes no sistema (`UserAlreadyExistException`).
- Autenticação via Token JWT para rotas protegidas.

###  Catálogo de Filmes (Cache-Aside Pattern)
- O sistema otimiza as requisições externas aplicando o padrão Cache-Aside.
- Ao buscar um filme (por Título ou IMDB ID), a API primeiro verifica o próprio Banco de Dados.
- Caso ocorra um *Cache Miss* (filme não encontrado localmente), a aplicação consome a **OMDB API**, formata os dados, salva no banco local para buscas futuras e retorna ao usuário.
- Tratamento de erro rigoroso caso o filme não exista nem no banco e nem na API externa (`MovieNotFoundedException`).

### Watchlist (Lista do Usuário)
- Usuários autenticados podem adicionar filmes às suas listas com nota, status (ex: WATCHED) e review.
- Regra de negócio blindada contra duplicatas: Um usuário não pode adicionar o mesmo filme duas vezes em sua lista (`MovieAlreadyInListException`).
- Visualização da própria lista e da lista de outros usuários.

###  Performance e Paginação
- Todos os endpoints que retornam listas (`getAllMovies`, `getMyList`, `lookListOfOthersUsers`) utilizam a interface `Pageable` e `Page` do Spring Data.
- Isso previne o esgotamento de memória do servidor e sobrecarga do banco de dados ao retornar grandes volumes de dados.

###  Resiliência (Global Exception Handler)
- Centralização do tratamento de erros através de um `@RestControllerAdvice`.
- O sistema intercepta exceções e as converte em respostas JSON padronizadas e amigáveis para o Front-end, escondendo a *stack trace* do servidor e retornando os *Status Codes* corretos:
    - `404 Not Found` (Filmes ou Usuários não encontrados).
    - `409 Conflict` (E-mails duplicados ou Filmes já existentes na lista).

##  Cobertura de Testes Unitários
A camada de serviços (`Services`), onde reside a inteligência da aplicação, possui cobertura de testes unitários utilizando **JUnit 5 e Mockito**.
- Testes garantindo o fluxo de sucesso .
- Testes validando todas as regras de bloqueio e lançamentos de exceções.
- Utilização de dublês (`@Mock`) para isolar as regras de banco de dados e APIs externas.
- Simulação de paginação em testes utilizando `PageImpl`.

##  Como rodar o projeto localmente

1. Clone este repositório:
```bash
git clone https://github.com/GeovannaMLima/Filmes_review.git
```

###  Executar via docker
1. Crie um arquivo chamado .env na raiz do projeto e adicione suas credenciais:
````bash
    DB_URL=jdbc:postgresql://seu-banco/cinema_db
    DB_USERNAME=seu_usuario
    DB_PASSWORD=sua_senha
    OMDB_API_KEY=sua_chave_aqui
    JWT_SECRET=sua_chave_secreta
````
2. Construa a imagem da aplicação e execute o contêiner injetando o arquivo de variáveis:
````bash
    docker build -t cinema-api .
    docker run -p 8080:8080 --env-file .env cinema-api
````

###  Executar via Maven
1. Configure o banco de dados no seu `application.properties`:
```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/cinema_db
   spring.datasource.username=seu_usuario_postgres
   spring.datasource.password=sua_senha_postgres
```

2. Adicione sua chave da API da OMDB no `application.properties`:
````bash
    api.omdb.key=sua_chave_aqui
````
3. Execute a aplicação via terminal (ou pela sua IDE):
````bash
    mvn spring-boot:run
````
---
Desenvolvido por [Geovanna Lima](https://www.linkedin.com/in/geovanna-lima0308)
* [LinkedIn](https://www.linkedin.com/in/geovanna-lima0308)
* [GitHub](https://github.com/GeovannaMLima)


   