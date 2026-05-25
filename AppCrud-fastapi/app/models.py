from sqlalchemy import String
from sqlalchemy.orm import Mapped, mapped_column

from .db import Base


class Aluno(Base):
    __tablename__ = "alunos"

    matricula: Mapped[str] = mapped_column(String(50), primary_key=True, index=True)
    nome: Mapped[str] = mapped_column(String(200), nullable=False)
