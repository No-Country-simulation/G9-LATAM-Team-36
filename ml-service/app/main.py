"""Bloque D — Microservicio ML (FastAPI). Ver Contrato 2 en Notion."""
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException

from app.model_loader import cargar_modelo
from app.predictor import predecir
from app.schemas import PredictRequest, PredictResponse

state = {"modelo": None}


@asynccontextmanager
async def lifespan(app: FastAPI):
    state["modelo"] = cargar_modelo()
    yield
    state["modelo"] = None


app = FastAPI(title="EnergiAI ML Service", lifespan=lifespan)


@app.get("/health")
def health():
    return {
        "status": "ok" if state["modelo"] is not None else "error",
        "model_loaded": state["modelo"] is not None,
    }


@app.post("/predict", response_model=PredictResponse)
def predict(req: PredictRequest):
    if state["modelo"] is None:
        raise HTTPException(status_code=503, detail="Modelo no disponible")
    return predecir(state["modelo"], req)
