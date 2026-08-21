"""Bloque D — Microservicio ML (FastAPI). Ver Contrato 2 en Notion."""
from contextlib import asynccontextmanager

from fastapi import FastAPI, HTTPException

from app.model_loader import MODEL_INFO, cargar_modelo
from app.predictor import predecir
from app.schemas import PredictRequest, PredictResponse

state = {"modelo": None}


@asynccontextmanager
async def lifespan(app: FastAPI):
    state["modelo"] = cargar_modelo()
    yield
    state["modelo"] = None


app = FastAPI(
    title="EnergiAI ML Service",
    description="Servicio de predicción del perfil energético (Contrato 2).",
    version="1.0.0",
    lifespan=lifespan,
)


@app.get("/health")
def health():
    """Estado del servicio y del modelo cargado (real vs dummy, versión y fuente)."""
    cargado = state["modelo"] is not None
    return {
        "status": "ok" if cargado else "error",
        "model_loaded": cargado,
        "model_source": MODEL_INFO["source"],
        "model_version": MODEL_INFO["version"],
        "is_dummy": MODEL_INFO["is_dummy"],
    }


@app.post("/predict", response_model=PredictResponse)
def predict(req: PredictRequest):
    if state["modelo"] is None:
        raise HTTPException(status_code=503, detail="Modelo no disponible")
    return predecir(state["modelo"], req)
