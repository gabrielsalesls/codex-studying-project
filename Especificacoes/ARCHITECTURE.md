# 🧠 Arquitetura

## 📦 Serviços

### user-service

Responsável por:

* Cadastro de usuários
* Validação de tipo (COMMON / MERCHANT)

---

### wallet-service

Responsável por:

* Gestão de saldo
* Débito e crédito

---

### transfer-service

Responsável por:

* Orquestrar transferências
* Garantir consistência

---

## 🗄️ Banco de Dados

Cada serviço possui seu próprio banco:

* user_db
* wallet_db
* transfer_db

---

## 🔗 Comunicação

Inicialmente:

* REST síncrono

Futuro:

* Eventos (mensageria)

---

## ⚠️ Consistência

Sem transação distribuída.

Estratégia:

* Compensação manual


## Arquitetura inicial desejada 

``` mermaid
flowchart TD

    %% Entrada
    FE[Frontend] --> GW[API Gateway]
    GW --> BFF[BFF Service]

    %% Cache no BFF
    BFF --> CACHE[(Redis Cache)]

    %% BFF orquestra (somente entrada)
    BFF --> TS[transfer-service]
    BFF --> US[user-service]
    BFF --> WS[wallet-service]

    %% Fluxo correto de criação de usuário
    US --> WS

    %% Fluxo de transferência
    TS --> US
    TS --> WS

    %% Banco por serviço
    US --> DBU[(user_db)]
    WS --> DBW[(wallet_db)]
    TS --> DBT[(transfer_db)]

    %% Autorizador externo
    TS --> AUTH[External Authorizer API]

    %% Mensageria
    TS -->|TransferCreated Event| MQ[(Message Broker)]

    %% Notificação
    MQ --> NS[notification-service]
    NS --> NOTIFY[External Notification API]

```