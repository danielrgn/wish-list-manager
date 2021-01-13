# WLM - Wish List Manager

[![Cicle-CI](https://circleci.com/gh/danielrgn/wish-list-manager.svg?style=shield)]()
[![codecov](https://codecov.io/gh/danielrgn/wish-list-manager/branch/master/graph/badge.svg?token=DJGIEESK6I)](https://codecov.io/gh/danielrgn/wish-list-manager)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](/LICENSE.md)

> Essa API é responsável gerenciar a lista de desejos dos clientes.

## Requisitos
```sh
Java 11
Docker Compose
Plugin Lombok
```

## Instalação OS X & Linux:


**Java 11 - SDKMAN:**

```sh
https://sdkman.io/install
sdk i java 11.0.2-open
```

**Docker compose:**

Acessar a pasta raiz do projeto e executar:

```sh
https://docs.docker.com/compose/install/
docker-compose up -d
```

**Lombok plugin:**

```sh
Intellij: https://projectlombok.org/setup/intellij
Eclipse : https://projectlombok.org/setup/eclipse
```

## Configuração para Desenvolvimento

Acessar a pasta raiz do projeto:

**Compilar o projeto:**

```sh
sdk use java 11.0.2-open
./mvnw clean package
```

**Executar o coverage:**

```sh
sdk use java 11.0.2-open
./mvnw clean install jacoco:report
```

**Executar o projeto com configuração local:**

```sh
sdk use java 11.0.2-open
./mvnw clean spring-boot:run -Dspring-boot.run.profiles=local
```

**Documentação da API:**

```
http://localhost:8080/swagger-ui.html#/
```

**Autenticação e Autorização:**

- Para todos os endpoints da aplicação é necessário o `Authorization: Bearer <access_token>`;
- Para conseguir se autenticar, é necessário fazer a requisição no endpoint `/oauth/token` para se obter o access_token (na collection do Postman abaixo possui um exemplo);
- Depois de conseguir o `<access_token>`, basta inclui-lo nas próximas requisições junto a palavra Bearer, exemplo: `Bearer 5588das-21c1-4580-8647-ds321dsaCss`

**Collection Postman:**

```
src/main/resources/WLM - Wish List Manager.postman_collection.json
```