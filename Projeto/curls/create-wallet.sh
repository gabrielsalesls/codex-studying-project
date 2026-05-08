#!/usr/bin/env bash

BASE_URL="${BASE_URL:-http://localhost:8082}"

curl --request POST \
  --url "${BASE_URL}/wallet" \
  --header "Content-Type: application/json" \
  --data '{
    "userId": 1,
    "userType": "COMMON"
  }'
