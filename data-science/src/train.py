"""
Bloque C — Entrenamiento del modelo (versión script del notebook 02).

Reproduce el modelo final elegido en 02_entrenamiento_modelos.ipynb (Random Forest,
n_estimators=100, max_depth=None) y guarda el pipeline completo + su metadata.
Con random_state=42 el resultado es reproducible.

Ejecutar desde la raíz del repo:
    python data-science/src/train.py
"""
import json
from datetime import datetime, timezone
from pathlib import Path

import joblib
import sklearn
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.metrics import accuracy_score, classification_report, f1_score
from sklearn.model_selection import cross_val_score, train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler

RANDOM_STATE = 42

# Columnas segun el Contrato 3 (los nombres son parte del contrato con el Bloque D).
FEATURES_CAT = ["tipo_inmueble"]
FEATURES_NUM = ["consumo_kwh", "cantidad_equipos", "horas_alto_consumo"]
FEATURES_BOOL = ["uso_horario_pico"]
FEATURES = FEATURES_CAT + FEATURES_NUM + FEATURES_BOOL
TARGET = "categoria"

# Hiperparametros del RF ganador (ver la comparacion en el notebook 02).
RF_PARAMS = {"n_estimators": 100, "max_depth": None}

RUTA_CSV = "data-science/data/dataset_energia.csv"
DIR_MODELS = Path("data-science/models")
RUTA_MODELO = DIR_MODELS / "modelo.joblib"
RUTA_META = DIR_MODELS / "metadata.json"


def build_pipeline() -> Pipeline:
    preproc = ColumnTransformer(
        [
            ("cat", OneHotEncoder(handle_unknown="ignore"), FEATURES_CAT),
            ("num", StandardScaler(), FEATURES_NUM),
        ],
        remainder="passthrough",  # deja pasar uso_horario_pico como 0/1
    )
    return Pipeline(
        [
            ("preproc", preproc),
            ("model", RandomForestClassifier(random_state=RANDOM_STATE, **RF_PARAMS)),
        ]
    )


def main():
    df = pd.read_csv(RUTA_CSV)
    X = df[FEATURES]
    y = df[TARGET]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, stratify=y, random_state=RANDOM_STATE
    )

    pipe = build_pipeline()
    pipe.fit(X_train, y_train)

    # Evaluacion
    pred = pipe.predict(X_test)
    acc = accuracy_score(y_test, pred)
    f1 = f1_score(y_test, pred, average="macro")
    cv = cross_val_score(pipe, X, y, cv=5, scoring="f1_macro")
    print(f"accuracy = {acc:.4f}  |  f1_macro = {f1:.4f}  |  "
          f"cv f1_macro = {cv.mean():.4f} +/- {cv.std():.4f}\n")
    print(classification_report(y_test, pred))

    # Serializacion del pipeline completo (nunca el modelo solo) + metadata
    DIR_MODELS.mkdir(parents=True, exist_ok=True)
    joblib.dump(pipe, RUTA_MODELO, compress=3)

    metadata = {
        "modelo": "RandomForestClassifier (pipeline con OneHot + StandardScaler)",
        "fecha_entrenamiento": datetime.now(timezone.utc).isoformat(),
        "sklearn_version": sklearn.__version__,
        "params": RF_PARAMS,
        "metricas_test": {"accuracy": round(acc, 4), "f1_macro": round(f1, 4)},
        "cv_f1_macro": {"mean": round(cv.mean(), 4), "std": round(cv.std(), 4)},
        "features": FEATURES,
        "clases": list(pipe.named_steps["model"].classes_),
        "n_registros_dataset": len(df),
    }
    RUTA_META.write_text(json.dumps(metadata, indent=2, ensure_ascii=False))

    tam_mb = RUTA_MODELO.stat().st_size / 1e6
    print(f"\nModelo guardado en {RUTA_MODELO} ({tam_mb:.2f} MB)")
    print(f"Metadata guardada en {RUTA_META}")


if __name__ == "__main__":
    main()
