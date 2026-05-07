# 🧩 User Stories

## 🟢 Fase 1 — Base

### 🟩 História 1 — Criar usuário

Como sistema, quero cadastrar usuários para que possam utilizar a plataforma.

**Critérios de aceitação:**

* CPF/email únicos
* Usuário criado com sucesso

---

### 🟩 História 2 — Criar carteira

Como sistema, quero que todo usuário tenha uma carteira com saldo inicial.

**Critérios de aceitação:**

* Carteira criada automaticamente
* Saldo inicial definido

---

## 🟢 Fase 2 — Consulta

### 🟩 História 3 — Consultar saldo

Como usuário, quero visualizar meu saldo.

**Critérios de aceitação:**

* Retorna saldo correto

---

### 🟩 História 4 — BFF agregando dados

Como frontend, quero receber dados consolidados.

**Critérios de aceitação:**

* Retorna usuário + saldo em uma única chamada

---

## 🔵 Fase 3 — Transferência

### 🟦 História 5 — Transferência básica

Como usuário, quero transferir dinheiro.

**Critérios de aceitação:**

* Saldo atualizado após transferência

---

### 🟦 História 6 — Validação de regras

Como sistema, quero validar regras antes da transferência.

**Critérios de aceitação:**

* Lojista não pode enviar
* Saldo suficiente

---

## 🔵 Fase 4 — Integração

### 🟦 História 7 — Autorizador externo

Como sistema, quero validar transferências externamente.

**Critérios de aceitação:**

* Bloqueia quando não autorizado

---

## 🔴 Fase 5 — Consistência

### 🟥 História 8 — Compensação

Como sistema, quero garantir consistência em falhas.

**Critérios de aceitação:**

* Saldo restaurado em erro

---

## 🟡 Fase 6 — Cache

### 🟨 História 9 — Cache no BFF

Como sistema, quero reduzir latência.

**Critérios de aceitação:**

* Cache HIT/MISS funcionando

---

## 🟣 Fase 7 — Observabilidade

### 🟪 História 10 — Logs e tracing

Como desenvolvedor, quero rastrear requisições.

**Critérios de aceitação:**

* Cada request possui traceId
* Logs estruturados
