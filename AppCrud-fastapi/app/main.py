from fastapi import Depends, FastAPI, HTTPException, status
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from . import crud, models, schemas
from .db import Base, SessionLocal, engine

app = FastAPI(title="Sistema de Gestao de Alunos")


def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


@app.on_event("startup")
def on_startup() -> None:
    Base.metadata.create_all(bind=engine)


@app.post("/alunos/", response_model=schemas.AlunoRead, status_code=status.HTTP_201_CREATED)
def create_aluno(aluno_in: schemas.AlunoCreate, db: Session = Depends(get_db)):
    try:
        return crud.create_aluno(db, aluno_in)
    except IntegrityError:
        raise HTTPException(
            status_code=status.HTTP_409_CONFLICT,
            detail="Matricula ja cadastrada",
        )


@app.get("/alunos/", response_model=list[schemas.AlunoRead])
def list_alunos(db: Session = Depends(get_db)):
    return crud.list_alunos(db)


@app.get("/alunos/{matricula}", response_model=schemas.AlunoRead)
def get_aluno(matricula: str, db: Session = Depends(get_db)):
    aluno = crud.get_aluno(db, matricula)
    if not aluno:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Aluno nao encontrado")
    return aluno


@app.put("/alunos/{matricula}", response_model=schemas.AlunoRead)
def update_aluno(matricula: str, aluno_in: schemas.AlunoUpdate, db: Session = Depends(get_db)):
    aluno = crud.get_aluno(db, matricula)
    if not aluno:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Aluno nao encontrado")
    return crud.update_aluno(db, aluno, aluno_in)


@app.delete("/alunos/{matricula}", status_code=status.HTTP_204_NO_CONTENT)
def delete_aluno(matricula: str, db: Session = Depends(get_db)):
    aluno = crud.get_aluno(db, matricula)
    if not aluno:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Aluno nao encontrado")
    crud.delete_aluno(db, aluno)
    return None
