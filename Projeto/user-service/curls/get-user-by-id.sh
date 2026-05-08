#!/usr/bin/env bash

BASE_URL="${BASE_URL:-http://localhost:8080}"
USER_ID="${1:-1}"

curl --request GET \
  --url "${BASE_URL}/api/v1/users/${USER_ID}" \
  --header "Content-Type: application/json"
