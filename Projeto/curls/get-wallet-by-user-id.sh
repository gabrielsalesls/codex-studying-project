#!/usr/bin/env bash

BASE_URL="${BASE_URL:-http://localhost:8082}"
USER_ID="${1:-1}"

curl --request GET \
  --url "${BASE_URL}/wallet/${USER_ID}" \
  --header "Content-Type: application/json"

curl --request GET \ --url "http://localhost:8082/wallet/11" \--header "Content-Type: application/json"