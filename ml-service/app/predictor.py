"""Bloque D — Convierte un request en predicción usando el modelo cargado."""
import pandas as pd

from app.schemas import PredictRequest, PredictResponse

FEATURES_ORDER = [
    "tipo_inmueble",
    "consumo_kwh",
    "cantidad_equipos",
    "horas_alto_consumo",
    "uso_horario_pico",
]


def predecir(modelo, req: PredictRequest) -> PredictResponse:
    fila = pd.DataFrame([{
        "tipo_inmueble": req.tipo_inmueble,
        "consumo_kwh": req.consumo_kwh,
        "cantidad_equipos": req.cantidad_equipos,
        "horas_alto_consumo": req.horas_alto_consumo,
        "uso_horario_pico": req.uso_horario_pico,
    }])[FEATURES_ORDER]

    proba = modelo.predict_proba(fila)[0]
    clases = list(getattr(modelo, "classes_", ["Eficiente", "Moderado", "Ineficiente"]))
    idx_max = max(range(len(proba)), key=lambda i: proba[i])

    return PredictResponse(
        categoria=clases[idx_max],
        probabilidad=round(float(proba[idx_max]), 2),
    )
