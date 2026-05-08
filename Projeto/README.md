# Projeto

Este projeto sobe as APIs `user-service` e `wallet-service` via Docker, sem necessidade de iniciar cada uma manualmente.

## Como rodar

Na raiz do projeto, execute:

```bash
docker compose up --build
```

## APIs disponiveis

Apos a subida dos containers:

* `user-service`: `http://localhost:8081`
* `wallet-service`: `http://localhost:8082`

## Scripts de curl

Os scripts para testes rapidos estao na pasta `curls/`:

* `curls/create-user.sh`: cria um usuario no `user-service` e dispara a criacao da carteira via integracao
* `curls/get-user-by-id.sh`: consulta um usuario por id
* `curls/get-user-error.sh`: consulta um usuario inexistente para validar o `404`
* `curls/create-wallet.sh`: cria uma carteira diretamente no `wallet-service`
* `curls/get-wallet-by-user-id.sh`: consulta o saldo da carteira pelo `userId`

## Como parar

Para encerrar os containers:

```bash
docker compose down
```
