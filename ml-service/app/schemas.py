"""Bloque D — Modelos Pydantic según el Contrato 2 (Backend <-> ML Service)."""
from typing import Literal

from pydantic import BaseModel, Field


class PredictRequest(BaseModel):
    consumo_kwh: float = Field(gt=0)
    uso_horario_pico: bool
    cantidad_equipos: int = Field(ge=1)
    tipo_inmueble: Literal["Casa", "Departamento", "Local"]
    horas_alto_consumo: int = Field(ge=0, le=24)


class PredictResponse(BaseModel):
    categoria: Literal["Eficiente", "Moderado", "Ineficiente"]
    probabilidad: float
