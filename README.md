# Projeto User Service

## Sobre o projeto Backend

Este documento README tem como objetivo fornecer as informações necessárias para documentação do projeto **Java Spring - Java 21+, Hibernate, PostgreSQL**.

## 🚨 Requisitos
- O projeto foi construído utilizando  **Java 21 LTS**,  **Spring Boot 3**, **JPA + Hibernate**, **JUnit 5**, **Maven 3** ;
- Instalar Java JDK 21 LTS (Open JDK / Eclipse Temurin Java JDK 21 LTS, instalar a versão Maven 3 para uso do terminal/console;

## 💻 Tecnologias
- Java 21 LTS
- Spring Boot 3 (Spring 6)
- JPA + Hibernate
- JUnit 5 + Mockito (back-end tests)
- Maven 3.2.x

  ## ⌨️ Editor / IDE
- IntelliJ IDEA - Community Edition [link](https://www.jetbrains.com/idea/download/download-thanks.html?platform=windows&code=IIC])
- IIntelliJ IDEA Ultimate [link](https://www.jetbrains.com/idea/download/download-thanks.html?platform=windows])

## Algumas Funcionalidades disponíveis na API

- ✅ Java model class with validation
- ✅ JPA repository
- ✅ JPA Pagination
- ✅ Controller, Service, and Repository layers
- ✅ Has-Many relationships (User entry)
- ✅ Java 21 LTS Records as DTO (Data Transfer Object)
- ✅ Hibernate / Jakarta Validation
- ✅ Unit tests for all layers (repository, service, controller)
- ✅ Test coverage for tests
- ✅ Spring Docs - Swagger (https://springdoc.org/)

# 🖥 O que foi desenvolvido?

O projeto trabalhar com gestão de usuários, perfils relacionamento entre usuários/perfils
- Para ter acesso as demais APIs precisamos enviar o **authorization** no header para autorizar a requisição;

### Entity User
- Salvar Usuário
- Listar usuários
- Buscar por id usuário
- Buscar por nome
- Atualizar usuário
- Deletar usuário

### Resources User
- Listar  | GET | api/v1/users | Status Code (200 (OK))
- Salvar  | POST | api/v1/users | Status Code (201 (OK)) 
- Buscar  | GET  | api/v1/users/{id} | Status Code (200 (OK))
- Atualizar | PUT | api/v1/users/{id} | Status Code (200 (OK))
- Deletar | DELETE | api/v1/users/{id} | Status Code (204 (No Content))

### Entity Profile
- Salvar profile
- Listar profiles
- Buscar por id
- Buscar por nome
- Atualizar
- Deletar profile

### Resources Profile
- Listar  | GET | api/v1/profiles | Status Code (200 (OK))
- Salvar  | POST | api/v1/profiles | Status Code (201 (OK))
- Buscar  | GET  | api/v1/profiles/{id} | Status Code (200 (OK))
- Atualizar | PUT | api/v1/profiles/{id} | Status Code (200 (OK))
- Deletar | DELETE | api/v1/profiles/{id} | Status Code (204 (No Content))

### Entity UserProfile
- Salvar UserProfile
- Listar UserProfile
- Buscar por id
- Buscar por nome
- Atualizar UserProfile
- Deletar UserProfile

### Resources UserProfile
- Listar  | GET | api/v1/user-profiles | Status Code (200 (OK))
- Salvar  | POST | api/v1/user-profiles | Status Code (201 (OK))
- Buscar  | GET  | api/v1/user-profiles/{id} | Status Code (200 (OK))
- Atualizar | PUT | api/v1/user-profiles/{id} | Status Code (200 (OK))
- Deletar | DELETE | api/v1/user-profiles/{id} | Status Code (204 (No Content))

## Execução do projeto

1. Executando tudo via `docker-compose`
2. Execução geral via docker-compose
- Basta executar o comando no diretório raiz do repositório:
`docker-compose up --build -d`

## Integração com API

- A documentação da API está disponível a partir de uma página web (http://localhost:8089/swagger-ui/index.html).

- **Servidor:** http://localhost:8089/api/v1/resource-name
- **Usuário:** admin
- **Senha:** 1234