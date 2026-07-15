"""Bloque D — Pruebas del microservicio ML con los ejemplos del brief."""
from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)

EJEMPLO_BRIEF = {
    "consumo_kwh": 420,
    "uso_horario_pico": True,
    "cantidad_equipos": 10,
    "tipo_inmueble": "Casa",
    "horas_alto_consumo": 8,
}


def test_health():
    r = client.get("/health")
    assert r.status_code == 200


def test_predict_ejemplo_brief():
    r = client.post("/predict", json=EJEMPLO_BRIEF)
    assert r.status_code == 200
    body = r.json()
    assert body["categoria"] in ["Eficiente", "Moderado", "Ineficiente"]
    assert 0 <= body["probabilidad"] <= 1


def test_predict_entrada_invalida():
    invalido = {**EJEMPLO_BRIEF, "consumo_kwh": -10}
    r = client.post("/predict", json=invalido)
    assert r.status_code == 422
