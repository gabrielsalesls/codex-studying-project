# 💳 PicPay Simplificado (Microservices Lab)

## 🎯 Objetivo

Construir uma plataforma de pagamentos simplificada com arquitetura de microserviços para estudar:

* Sistemas distribuídos
* Observabilidade
* Resiliência
* AWS

---

## 🧱 Arquitetura Inicial

Serviços:

* user-service
* wallet-service
* transfer-service

---

## 🔁 Fluxo principal

1. Receber transferência
2. Validar usuário
3. Validar saldo
4. Consultar autorizador externo
5. Debitar/creditar saldo
6. Registrar transação

---

## ⚠️ Regras importantes

* Lojistas não enviam dinheiro
* Deve haver saldo suficiente
* Operação deve ser consistente (com compensação)
* Autorizador externo pode falhar

---

## 🚀 Como rodar

```bash
docker-compose up --build
```

---

## 🧠 Estratégia

Começar simples e evoluir:

1. Comunicação síncrona
2. Observabilidade
3. Resiliência
4. Mensageria
5. AWS
