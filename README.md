# AppCrud-fastapi

API REST para cadastro de alunos usando FastAPI + SQLAlchemy.

## Requisitos

- Python 3.11+

## Instalacao

```bash
python -m venv .venv
.venv\\Scripts\\activate
pip install -r requirements.txt
```

## Executar localmente

```bash
uvicorn app.main:app --reload
```

Acesse a documentacao em: http://127.0.0.1:8000/docs

## Variaveis de ambiente

- `DATABASE_URL`: URL do banco Postgres (Railway injeta automaticamente). Em local, se nao informar, usa SQLite em `./app.db`.

## Endpoints

- `POST /alunos/`
- `GET /alunos/`
- `GET /alunos/{matricula}`
- `PUT /alunos/{matricula}`
- `DELETE /alunos/{matricula}`

## Deploy no Railway (Nixpacks)

1. Suba o repo para GitHub/GitLab.
2. Railway: New Project -> Deploy from GitHub -> selecione o repo.
3. Adicione um Postgres: New -> Database -> PostgreSQL.
4. Garanta a variavel `DATABASE_URL` (Railway cria automaticamente ao adicionar o Postgres).
5. Start command recomendado:

```bash
uvicorn app.main:app --host 0.0.0.0 --port $PORT
```

Depois do deploy, acesse `/docs` na URL publica.