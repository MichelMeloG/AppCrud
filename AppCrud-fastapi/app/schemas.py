from pydantic import BaseModel, Field


class AlunoBase(BaseModel):
    nome: str = Field(..., min_length=1)


class AlunoCreate(AlunoBase):
    matricula: str = Field(..., min_length=1, max_length=50)


class AlunoUpdate(BaseModel):
    nome: str = Field(..., min_length=1)


class AlunoRead(AlunoBase):
    matricula: str

    class Config:
        from_attributes = True
