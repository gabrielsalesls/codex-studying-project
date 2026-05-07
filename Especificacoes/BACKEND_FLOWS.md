# 🔁 Backend Flows — PicPay Simplificado

## 🎯 Objetivo

Documentar de forma simples e direta os fluxos principais do backend para facilitar:

* entendimento do sistema
* implementação
* debugging
* onboarding de novos desenvolvedores

---

# 🟢 Fluxo de Cadastro

```text
Usuário clica em cadastrar → BFF chama user-service → user-service valida CPF/email → salva usuário → chama wallet-service → cria carteira → retorna sucesso
```

### ✅ Resultado

* Usuário criado
* Carteira criada automaticamente

---

# 🟡 Fluxo de Consulta (Home)

```text
Usuário abre home → BFF verifica cache → (MISS) chama user-service → chama wallet-service → agrega dados → salva no cache → retorna resposta
```

### ✅ Resultado

* Dados do usuário + saldo retornados
* Possível ganho de performance com cache

---

# 🔵 Fluxo de Transferência (Principal)

```text
Usuário clica em transferir → BFF chama transfer-service → transfer-service chama user-service (validação) → chama wallet-service (saldo) → chama autorizador externo → debita payer → credita payee → salva transação → retorna sucesso
```

### ✅ Resultado

* Transferência realizada com sucesso
* Saldo atualizado

---

# 🔴 Fluxo de Transferência com Erro

```text
Usuário inicia transferência → BFF chama transfer-service → validações falham (saldo, tipo de usuário, autorizador) → retorna erro → nenhuma alteração no saldo
```

### ✅ Resultado

* Operação abortada com segurança
* Nenhuma inconsistência no sistema

---

# 💥 Fluxo de Compensação

```text
Transferência inicia → débito realizado → falha antes do crédito → transfer-service detecta erro → chama wallet-service → reverte valor → marca transação como COMPENSATED
```

### ✅ Resultado

* Sistema retorna a um estado consistente
* Transação registrada como compensada

---

# ⚡ Fluxo de Cache (BFF)

```text
BFF recebe request → verifica cache → HIT retorna direto → MISS chama serviços → salva no cache → retorna resposta
```

### ✅ Resultado

* Redução de latência
* Menor carga nos serviços

---

# 🌐 Fluxo de Autorizador Externo

```text
transfer-service chama API externa → recebe autorização → (true) continua fluxo → (false) aborta transferência
```

### ✅ Resultado

* Transferências validadas externamente
* Controle adicional de segurança

---

# 📩 Fluxo de Notificação (Assíncrono)

```text
transfer-service conclui transferência → publica evento → mensageria → notification-service consome → chama serviço externo → envia notificação
```

### ✅ Resultado

* Usuário recebe confirmação
* Processo desacoplado do fluxo principal

---

# 🧠 Resumo Geral

```text
Cadastro → Consulta → Transferência → (Erro ou Sucesso) → Notificação → Atualização de saldo
```

---

# 📌 Observações

* Todos os serviços possuem banco próprio
* Não há transação distribuída
* Consistência é garantida via compensação
* Comunicação síncrona + assíncrona

---

# 🚀 Uso recomendado

* Consultar este arquivo antes de implementar features
* Utilizar como base para testes de integração
* Atualizar sempre que um fluxo mudar
