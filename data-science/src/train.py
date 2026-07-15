"""
Bloque C — Entrenamiento del modelo (versión script del notebook 02).

Ejecutar:
    python src/train.py
"""
import joblib
import pandas as pd
from sklearn.compose import ColumnTransformer
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import OneHotEncoder, StandardScaler

FEATURES_CAT = ["tipo_inmueble"]
FEATURES_NUM = ["consumo_kwh", "cantidad_equipos", "horas_alto_consumo"]
FEATURES_BOOL = ["uso_horario_pico"]
TARGET = "categoria"


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
            ("model", RandomForestClassifier(random_state=42)),
        ]
    )


def main():
    df = pd.read_csv("data-science/data/dataset_energia.csv")
    X = df[FEATURES_CAT + FEATURES_NUM + FEATURES_BOOL]
    y = df[TARGET]

    X_train, X_test, y_train, y_test = train_test_split(
        X, y, test_size=0.2, stratify=y, random_state=42
    )

    pipe = build_pipeline()
    pipe.fit(X_train, y_train)

    # TODO (Bloque C): evaluar con classification_report, F1 macro,
    # matriz de confusión y comparar contra LogisticRegression baseline.

    joblib.dump(pipe, "data-science/models/modelo.joblib")
    print("Modelo entrenado y guardado en data-science/models/modelo.joblib")


if __name__ == "__main__":
    main()
