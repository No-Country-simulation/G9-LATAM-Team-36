"""
Bloque D — Carga del modelo entrenado (Bloque C).

Orden de resolución:
  1. Si USE_LOCAL_MODEL=true (default): busca modelo.joblib en rutas locales
     (MODEL_PATH, junto al servicio, o data-science/models al correr desde la raíz).
  2. Si USE_LOCAL_MODEL=false: lo descarga de OCI Object Storage.
  3. Si no encuentra nada: usa DummyModel (reglas simples) para no bloquear al
     Bloque E durante la integración temprana.

`MODEL_INFO` expone la procedencia y versión del modelo cargado (lo usa /health).
"""
import json
import os
from pathlib import Path

import joblib

# Info del modelo actualmente cargado (la lee el endpoint /health).
MODEL_INFO = {"source": None, "version": None, "is_dummy": True}


class DummyModel:
    """Modelo de relleno: reglas simples, solo para desbloquear integración temprana."""

    classes_ = ["Eficiente", "Moderado", "Ineficiente"]

    def predict_proba(self, X):
        row = X.iloc[0]
        if row["consumo_kwh"] > 400 or row["horas_alto_consumo"] > 8:
            return [[0.05, 0.14, 0.81]]  # Ineficiente
        if row["consumo_kwh"] > 250:
            return [[0.15, 0.65, 0.20]]  # Moderado
        return [[0.80, 0.15, 0.05]]  # Eficiente


def _rutas_candidatas() -> list[Path]:
    """Rutas donde buscar el modelo real, en orden de prioridad."""
    candidatas = []
    if os.getenv("MODEL_PATH"):
        candidatas.append(Path(os.environ["MODEL_PATH"]))
    candidatas += [
        Path("models/modelo.joblib"),               # copiado/montado junto al servicio
        Path("data-science/models/modelo.joblib"),  # corriendo desde la raíz del monorepo
        # Raíz del repo calculada desde este archivo (ml-service/app/model_loader.py)
        Path(__file__).resolve().parents[2] / "data-science" / "models" / "modelo.joblib",
    ]
    return candidatas


def _leer_version(ruta_modelo: Path) -> str | None:
    """Lee metadata.json junto al modelo para reportar su versión/fecha."""
    meta = ruta_modelo.with_name("metadata.json")
    if meta.exists():
        try:
            data = json.loads(meta.read_text())
            return data.get("fecha_entrenamiento") or data.get("sklearn_version")
        except (json.JSONDecodeError, OSError):
            return None
    return None


def _cargar_local() -> object | None:
    """Devuelve el modelo real si lo encuentra en disco, o None."""
    for ruta in _rutas_candidatas():
        if ruta.exists():
            modelo = joblib.load(ruta)
            MODEL_INFO.update(source=f"local:{ruta}", version=_leer_version(ruta),
                              is_dummy=False)
            print(f"Modelo real cargado desde {ruta}")
            return modelo
    return None


def _descargar_de_oci() -> object:
    """Descarga modelo.joblib desde OCI Object Storage y lo carga."""
    import oci

    bucket = os.environ["OCI_BUCKET"]
    object_name = os.getenv("MODEL_OBJECT_NAME", "modelo.joblib")
    config = oci.config.from_file(profile_name=os.getenv("OCI_CONFIG_PROFILE", "DEFAULT"))
    client = oci.object_storage.ObjectStorageClient(config)
    namespace = os.getenv("OCI_NAMESPACE") or client.get_namespace().data

    destino = Path("/tmp/modelo.joblib")
    resp = client.get_object(namespace, bucket, object_name)
    destino.write_bytes(resp.data.content)
    modelo = joblib.load(destino)
    MODEL_INFO.update(source=f"oci:{bucket}/{object_name}", version=None, is_dummy=False)
    print(f"Modelo real descargado de OCI ({bucket}/{object_name})")
    return modelo


def cargar_modelo():
    """Carga el mejor modelo disponible según la configuración del entorno."""
    use_local = os.getenv("USE_LOCAL_MODEL", "true").lower() == "true"

    if use_local:
        modelo = _cargar_local()
        if modelo is not None:
            return modelo
        print("⚠️  Modelo real no encontrado en disco, usando DummyModel")
        MODEL_INFO.update(source="dummy", version="dummy", is_dummy=True)
        return DummyModel()

    try:
        return _descargar_de_oci()
    except Exception as e:  # noqa: BLE001 — cualquier fallo de OCI cae a DummyModel
        print(f"⚠️  No se pudo descargar de OCI ({e}), usando DummyModel")
        MODEL_INFO.update(source="dummy", version="dummy", is_dummy=True)
        return DummyModel()
