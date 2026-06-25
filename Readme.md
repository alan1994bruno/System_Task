# System Tasks 📋

_(For the English version, scroll down)_

Um sistema de gerenciamento de tarefas via terminal (CLI) inspirado no modelo Kanban. Construído para garantir rastreabilidade rigorosa de tarefas, com regras estritas de movimentação, auditoria de bloqueios e relatórios de tempo (Lead Time).

## 🚀 Funcionalidades

- **Gerenciamento de Boards:** Criação, listagem com status de progresso, seleção e exclusão estruturada (Cascade Delete).
- **Controle de Colunas:** Regras de negócio rígidas garantindo colunas Iniciais, Finais e de Cancelamento.
- **Gestão de Cards:** Navegação obrigatória sequencial (sem pular etapas), com exceção para a coluna de Cancelamento.
- **Sistema de Bloqueio:** Bloqueio e desbloqueio de cards exigindo justificativa obrigatória, impedindo movimentações enquanto bloqueado.
- **Relatórios Avançados:**
- **Lead Time:** Tempo exato gasto por um card em cada coluna.
- **Auditoria de Bloqueios:** Histórico de justificativas e tempo total em que as tarefas ficaram paralisadas.

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3 (Spring Data JPA, Injeção de Dependências)
- **Banco de Dados:** MySQL 8.0
- **Migrations:** Flyway
- **Build Tool:** Gradle (Kotlin DSL)
- **Infraestrutura:** Docker & Docker Compose

## ⚙️ Pré-requisitos

- JDK 21 (Amazon Corretto recomendado)
- Docker e Docker Compose
- Acesso a um terminal

## 🏃 Como Executar

1. Suba o banco de dados MySQL via Docker:

```bash
docker-compose up -d

```

2. Compile e execute a aplicação via Gradle:

```bash
./gradlew bootRun

```

_(O Flyway rodará as migrations automaticamente na primeira execução)._ 3. Interaja com o menu diretamente no seu terminal!

---

# System Tasks 📋

A terminal-based (CLI) task management system inspired by the Kanban model. Built to ensure rigorous task traceability, with strict movement rules, block auditing, and time reporting (Lead Time).

## 🚀 Features

- **Board Management:** Creation, listing with progress status, selection, and structured deletion (Cascade Delete).
- **Column Control:** Strict business rules ensuring Initial, Final, and Cancellation columns.
- **Card Management:** Mandatory sequential navigation (no skipping steps), with an exception for the Cancellation column.
- **Blocking System:** Card blocking and unblocking requiring mandatory justification, preventing movement while blocked.
- **Advanced Reports:**
- **Lead Time:** Exact time spent by a card in each column.
- **Block Audit:** History of justifications and the total time tasks were stalled.

## 🛠️ Technologies Used

- **Language:** Java 21
- **Framework:** Spring Boot 3 (Spring Data JPA, Dependency Injection)
- **Database:** MySQL 8.0
- **Migrations:** Flyway
- **Build Tool:** Gradle (Kotlin DSL)
- **Infrastructure:** Docker & Docker Compose

## ⚙️ Prerequisites

- JDK 21 (Amazon Corretto recommended)
- Docker and Docker Compose
- Terminal access

## 🏃 How to Run

1. Spin up the MySQL database via Docker:

```bash
docker-compose up -d

```

2. Build and run the application via Gradle:

```bash
./gradlew bootRun

```

_(Flyway will automatically run the migrations on the first execution)._ 3. Interact with the menu directly in your terminal!
