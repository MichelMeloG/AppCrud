import os

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker, DeclarativeBase


def _build_database_url() -> str:
    url = os.getenv("DATABASE_URL")
    if url:
        # Railway provides postgres://, SQLAlchemy expects postgresql://
        return url.replace("postgres://", "postgresql://", 1)
    return "sqlite:///./app.db"


DATABASE_URL = _build_database_url()

engine = create_engine(
    DATABASE_URL,
    connect_args={"check_same_thread": False} if DATABASE_URL.startswith("sqlite") else {},
)

SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False)


class Base(DeclarativeBase):
    pass
