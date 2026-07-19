"""Bloque D — Pruebas del microservicio ML con los ejemplos del brief.

Nota (arreglo temporal de CI): el TestClient se usa dentro de un `with` (vía
fixture) para que se ejecute el `lifespan` de FastAPI y se cargue el modelo en
`state`. Sin esto, `state["modelo"]` queda en None y `/predict` responde 503.
Mientras no exista el modelo real de data-science (Bloque C), `cargar_modelo()`
devuelve el DummyModel, suficiente para que estas pruebas pasen.
"""
import pytest
from fastapi.testclient import TestClient

from app.main import app

EJEMPLO_BRIEF = {
    "consumo_kwh": 420,
    "uso_horario_pico": True,
    "cantidad_equipos": 10,
    "tipo_inmueble": "Casa",
    "horas_alto_consumo": 8,
}


@pytest.fixture
def client():
    # El bloque `with` dispara startup/shutdown (lifespan) -> carga el modelo.
    with TestClient(app) as c:
        yield c


def test_health(client):
    r = client.get("/health")
    assert r.status_code == 200
    assert r.json()["model_loaded"] is True


def test_predict_ejemplo_brief(client):
    r = client.post("/predict", json=EJEMPLO_BRIEF)
    assert r.status_code == 200
    body = r.json()
    assert body["categoria"] in ["Eficiente", "Moderado", "Ineficiente"]
    assert 0 <= body["probabilidad"] <= 1


def test_predict_entrada_invalida(client):
    invalido = {**EJEMPLO_BRIEF, "consumo_kwh": -10}
    r = client.post("/predict", json=invalido)
    assert r.status_code == 422
