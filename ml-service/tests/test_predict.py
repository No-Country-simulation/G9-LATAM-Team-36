"""Bloque D — Pruebas del microservicio ML (Contrato 2).

El TestClient se usa dentro de un `with` (vía fixture) para que se ejecute el
`lifespan` de FastAPI y se cargue el modelo en `state`. Los perfiles de prueba
están elegidos para dar la misma clase con el modelo real y con el DummyModel,
así los tests son estables aunque el modelo no esté presente.
"""
import pytest
from fastapi.testclient import TestClient

from app.main import app

# Perfiles de prueba (misma clase con modelo real y con DummyModel)
EJEMPLO_BRIEF = {  # -> Ineficiente
    "consumo_kwh": 420,
    "uso_horario_pico": True,
    "cantidad_equipos": 10,
    "tipo_inmueble": "Casa",
    "horas_alto_consumo": 8,
}
PERFIL_EFICIENTE = {  # -> Eficiente
    "consumo_kwh": 120,
    "uso_horario_pico": False,
    "cantidad_equipos": 5,
    "tipo_inmueble": "Departamento",
    "horas_alto_consumo": 2,
}
PERFIL_MODERADO = {  # -> Moderado
    "consumo_kwh": 400,
    "uso_horario_pico": False,
    "cantidad_equipos": 8,
    "tipo_inmueble": "Casa",
    "horas_alto_consumo": 7,
}


@pytest.fixture
def client():
    # El bloque `with` dispara startup/shutdown (lifespan) -> carga el modelo.
    with TestClient(app) as c:
        yield c


def test_health(client):
    r = client.get("/health")
    assert r.status_code == 200
    body = r.json()
    assert body["status"] == "ok"
    assert body["model_loaded"] is True
    # /health debe exponer la trazabilidad del modelo
    assert set(body) >= {"status", "model_loaded", "model_source", "is_dummy"}


def test_predict_ejemplo_brief(client):
    r = client.post("/predict", json=EJEMPLO_BRIEF)
    assert r.status_code == 200
    body = r.json()
    assert body["categoria"] == "Ineficiente"
    assert 0 <= body["probabilidad"] <= 1


def test_predict_respeta_contrato2(client):
    """La respuesta debe tener exactamente los campos del Contrato 2."""
    body = client.post("/predict", json=EJEMPLO_BRIEF).json()
    assert set(body) == {"categoria", "probabilidad"}


@pytest.mark.parametrize(
    "perfil, esperado",
    [
        (EJEMPLO_BRIEF, "Ineficiente"),
        (PERFIL_EFICIENTE, "Eficiente"),
        (PERFIL_MODERADO, "Moderado"),
    ],
)
def test_predict_clasifica_cada_perfil(client, perfil, esperado):
    body = client.post("/predict", json=perfil).json()
    assert body["categoria"] == esperado


def test_predict_perfiles_dan_clases_distintas(client):
    """Perfiles contrastantes deben producir clases distintas."""
    cats = {
        client.post("/predict", json=p).json()["categoria"]
        for p in (EJEMPLO_BRIEF, PERFIL_EFICIENTE, PERFIL_MODERADO)
    }
    assert len(cats) == 3


def test_predict_consumo_invalido(client):
    invalido = {**EJEMPLO_BRIEF, "consumo_kwh": -10}
    r = client.post("/predict", json=invalido)
    assert r.status_code == 422


def test_predict_tipo_inmueble_invalido(client):
    invalido = {**EJEMPLO_BRIEF, "tipo_inmueble": "Bodega"}
    r = client.post("/predict", json=invalido)
    assert r.status_code == 422
