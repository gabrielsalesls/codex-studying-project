# 🔁 Fluxos do Sistema

## 🧾 1. Cadastro de Usuário

### 🎯 Objetivo

Criar um usuário e garantir que ele tenha uma carteira.

### 🔄 Fluxo

1. Frontend chama BFF
2. BFF chama `user-service`
3. `user-service` valida CPF/email
4. Usuário é salvo no `user_db`
5. `wallet-service` cria carteira
6. Carteira é salva no `wallet_db`

---

## 💰 2. Consulta de Usuário + Saldo

### 🎯 Objetivo

Retornar dados agregados para o frontend.

### 🔄 Fluxo

1. Frontend chama BFF
2. BFF verifica cache
3. BFF chama `user-service`
4. BFF chama `wallet-service`
5. BFF agrega resposta
6. Retorna resposta única

---

## 🔁 3. Transferência (CORE)

### 🎯 Objetivo

Transferir dinheiro entre usuários.

### 🔄 Fluxo

1. Frontend chama BFF
2. BFF chama `transfer-service`
3. `transfer-service` valida usuário (`user-service`)
4. `transfer-service` valida saldo (`wallet-service`)
5. `transfer-service` chama autorizador externo
6. `transfer-service` debita saldo
7. `transfer-service` credita saldo
8. `transfer-service` registra transação

---

## 💥 4. Falha e Compensação

### 🎯 Objetivo

Garantir consistência em falhas.

### 🔄 Fluxo

1. Débito realizado
2. Falha antes do crédito
3. `transfer-service` detecta erro
4. Executa compensação:

   * devolve saldo ao pagador

---

## ⚡ 5. Cache no BFF

### 🎯 Objetivo

Melhorar performance em consultas.

### 🔄 Fluxo

1. BFF recebe requisição
2. Verifica cache

   * HIT → retorna
   * MISS → chama serviço
3. Armazena resposta no cache

---

## 🌐 6. Autorizador Externo

### 🎯 Objetivo

Validar transferência antes de executar.

### 🔄 Fluxo

1. `transfer-service` chama API externa
2. Se autorizado → continua
3. Se negado → aborta operação
