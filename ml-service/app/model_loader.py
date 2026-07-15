"""
Bloque D — Carga del modelo entrenado.

Fase inicial (Semana 1): usa DummyModel para que el Bloque E pueda
integrarse sin esperar al modelo real del Bloque C.
Fase real (Semana 2+): descarga modelo.joblib desde OCI Object Storage
o lo lee localmente si USE_LOCAL_MODEL=true.
"""
import os

import joblib


class DummyModel:
    """Modelo de relleno: reglas simples, solo para desbloquear integración temprana."""

    classes_ = ["Eficiente", "Moderado", "Ineficiente"]

    def predict_proba(self, X):
        # TODO (Bloque D): reemplazar con el modelo real de data-science/models/modelo.joblib
        row = X.iloc[0]
        if row["consumo_kwh"] > 400 or row["horas_alto_consumo"] > 8:
            return [[0.05, 0.14, 0.81]]  # Ineficiente
        if row["consumo_kwh"] > 250:
            return [[0.15, 0.65, 0.20]]  # Moderado
        return [[0.80, 0.15, 0.05]]  # Eficiente


def cargar_modelo():
    use_local = os.getenv("USE_LOCAL_MODEL", "true").lower() == "true"

    if use_local:
        local_path = "data-science/models/modelo.joblib"
        if os.path.exists(local_path):
            return joblib.load(local_path)
        print("⚠️  Modelo real no encontrado, usando DummyModel")
        return DummyModel()

    # TODO (Bloque D): descargar desde OCI Object Storage usando el SDK `oci`
    # namespace = os.environ["OCI_NAMESPACE"]
    # bucket = os.environ["OCI_BUCKET"]
    # object_name = os.environ["MODEL_OBJECT_NAME"]
    # ... descargar a /tmp/modelo.joblib y joblib.load(...)
    raise NotImplementedError("Bloque D: implementar descarga desde OCI Object Storage")
