# AppCrud

Projeto de CRUD de alunos com backend em FastAPI + SQLAlchemy e app Android.

## Visao geral

- Cadastro completo de alunos (criar, listar, editar, remover).
- API REST documentada via Swagger.
- App Android consumindo a API.

## Estrutura do repositorio

- Backend (FastAPI): [AppCrud-fastapi/app/](AppCrud-fastapi/app/)
- Dependencias (API): [AppCrud-fastapi/requirements.txt](AppCrud-fastapi/requirements.txt)
- App Android: [appmobile/](appmobile/)
- Capturas de tela: [prints/](prints/)

## Como executar a API localmente

Requisitos: Python 3.11+

```bash
cd AppCrud-fastapi
python -m venv .venv
.venv\\Scripts\\activate
pip install -r requirements.txt
```

```bash
uvicorn app.main:app --reload
```

Se estiver usando Python 3.14, use Python 3.12 para evitar problemas de compatibilidade com dependencias nativas.

Documentacao da API (Swagger): http://127.0.0.1:8000/docs

## Variaveis de ambiente

- `DATABASE_URL`: URL do banco. Em local, se nao informar, usa SQLite em `./app.db`.

### MySQL local (PyMySQL)

Exemplo de `DATABASE_URL`:

```bash
mysql+pymysql://usuario:senha@localhost:3306/nome_do_banco
```

Defina a variavel antes de iniciar a API.

PowerShell:

```powershell
$env:DATABASE_URL = "mysql+pymysql://usuario:senha@localhost:3306/nome_do_banco"
```

CMD:

```bat
set DATABASE_URL=mysql+pymysql://usuario:senha@localhost:3306/nome_do_banco
```

### MySQL Workbench (local)

1. Crie o schema no MySQL Workbench.
2. Atualize o `DATABASE_URL` com usuario, senha, host e o schema criado.
3. Inicie a API e valide em `/docs`.

## Endpoints principais

- `POST /alunos/`
- `GET /alunos/`
- `GET /alunos/{matricula}`
- `PUT /alunos/{matricula}`
- `DELETE /alunos/{matricula}`

## App Android

O app fica em [appmobile/](appmobile/). Abra o projeto no Android Studio, configure o endereco da API se necessario e execute no emulador ou dispositivo.

## Capturas de tela

<p align="center">
	<img src="prints/Tela%20inicial.png" alt="Tela inicial">
	<br>
	<em>Tela principal do app com a lista de alunos e acesso rapido as acoes.</em>
</p>

<p align="center">
	<img src="prints/Criar%20aluno.png" alt="Criar aluno">
	<br>
	<em>Formulario de cadastro de aluno com os campos obrigatorios.</em>
</p>

<p align="center">
	<img src="prints/Aluno%20criado.png" alt="Aluno criado">
	<br>
	<em>Confirmacao do aluno criado e exibicao na lista.</em>
</p>

<p align="center">
	<img src="prints/Editar%20aluno.png" alt="Editar aluno">
	<br>
	<em>Tela de edicao para atualizar os dados do aluno.</em>
</p>

<p align="center">
	<img src="prints/Print%20swagger.png" alt="Swagger">
	<br>
	<em>Documentacao da API no Swagger, com endpoints e testes.</em>
</p>

<p align="center">
	<img src="prints/Print%20mysql.png" alt="MySQL">
	<br>
	<em>Banco MySQL no Workbench com a tabela de alunos persistida.</em>
</p>

