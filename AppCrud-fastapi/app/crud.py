from sqlalchemy import select
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import Session

from . import models, schemas


def create_aluno(db: Session, aluno_in: schemas.AlunoCreate) -> models.Aluno:
    aluno = models.Aluno(matricula=aluno_in.matricula, nome=aluno_in.nome)
    db.add(aluno)
    try:
        db.commit()
    except IntegrityError:
        db.rollback()
        raise
    db.refresh(aluno)
    return aluno


def list_alunos(db: Session) -> list[models.Aluno]:
    return list(db.scalars(select(models.Aluno)).all())


def get_aluno(db: Session, matricula: str) -> models.Aluno | None:
    return db.get(models.Aluno, matricula)


def update_aluno(db: Session, aluno: models.Aluno, aluno_in: schemas.AlunoUpdate) -> models.Aluno:
    aluno.nome = aluno_in.nome
    db.add(aluno)
    db.commit()
    db.refresh(aluno)
    return aluno


def delete_aluno(db: Session, aluno: models.Aluno) -> None:
    db.delete(aluno)
    db.commit()
