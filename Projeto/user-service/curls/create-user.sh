#!/usr/bin/env bash

BASE_URL="${BASE_URL:-http://localhost:8080}"

curl --request POST \
  --url "${BASE_URL}/users" \
  --header "Content-Type: application/json" \
  --data '{
    "name": "Maria Silva",
    "cpf": "12345678901",
    "email": "maria.silva@example.com",
    "password": "secret123",
    "type": "COMMON"
  }'
