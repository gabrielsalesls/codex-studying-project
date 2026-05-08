# CURLs de teste

Os exemplos desta pasta assumem que a aplicacao esta rodando em `http://localhost:8080`.
Todas as rotas usam o prefixo `/api/v1`.

Arquivos:

- `create-user.sh`: cria um usuario.
- `get-user-by-id.sh`: busca um usuario por id.
- `get-user-error.sh`: busca um usuario inexistente para validar o retorno `404`.

Se quiser alterar a porta, ajuste a variavel `BASE_URL` em cada arquivo.
