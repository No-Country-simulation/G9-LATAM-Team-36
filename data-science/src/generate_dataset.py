"""
Bloque A — Generación del dataset simulado de consumo energético.

Genera un CSV con el esquema del Contrato 3:
consumo_kwh, uso_horario_pico, cantidad_equipos, tipo_inmueble,
horas_alto_consumo, categoria

Ejecutar:
    python src/generate_dataset.py
"""
import numpy as np
import pandas as pd

from labeling_rules import etiquetar

RNG = np.random.default_rng(seed=42)
N_REGISTROS = 4000

# Ruta de salida, relativa a la raiz del repo (ejecutar desde ahi).
RUTA_CSV = "data-science/data/dataset_energia.csv"

# Ninguna categoria debe quedar por debajo de este porcentaje (DoD del Bloque A).
BALANCE_MINIMO = 20.0

# --- Parametros de simulacion por tipo de inmueble ---
# Los tres tipos y su peso en el muestreo (deben sumar 1.0).
TIPOS = ["Casa", "Departamento", "Local"]
PESOS_TIPO = [0.45, 0.40, 0.15]

# consumo_kwh: distribucion normal truncada por tipo (media, desviacion).
CONSUMO_PARAMS = {
    "Casa": (350, 120),
    "Departamento": (220, 80),
    "Local": (600, 200),
}
CONSUMO_MINIMO = 30  # kWh; ningun inmueble consume menos que esto

# cantidad_equipos: Poisson por tipo (lambda), desplazada a minimo 1.
EQUIPOS_LAMBDA = {
    "Casa": 9,
    "Departamento": 6,
    "Local": 15,
}
EQUIPOS_MINIMO = 1

# uso_horario_pico: la probabilidad de pico crece con el consumo. Se modela con
# una logistica sobre el consumo estandarizado; PICO_K controla que tan marcada
# es esa relacion (a mayor K, mas separacion entre bajos y altos consumos).
PICO_K = 1.2

# Outliers: fraccion de registros con consumo extremo (a proposito, para el EDA).
OUTLIERS_FRAC = 0.04
OUTLIER_FACTOR = (2.5, 4.0)  # rango del multiplicador de consumo


def _sigmoide(x: np.ndarray) -> np.ndarray:
    return 1.0 / (1.0 + np.exp(-x))


def _normal_truncada(rng, media, sigma, tam, minimo):
    """Normal(media, sigma) re-muestreando los valores por debajo de `minimo`.

    Se re-muestrea (en vez de recortar con clip) para no acumular masa
    artificial justo en el valor minimo y conservar una forma realista.
    """
    valores = rng.normal(media, sigma, size=tam)
    bajo_minimo = valores < minimo
    while bajo_minimo.any():
        valores[bajo_minimo] = rng.normal(media, sigma, size=bajo_minimo.sum())
        bajo_minimo = valores < minimo
    return valores


def generar_registros(n: int = N_REGISTROS) -> pd.DataFrame:
    """Genera `n` registros de consumo simulado (sin etiquetar todavia).

    Devuelve un DataFrame con las features de entrada del Contrato 3.
    La columna `categoria` la agrega despues labeling_rules.etiquetar().
    """
    # tipo_inmueble: muestreo ponderado
    tipo_inmueble = RNG.choice(TIPOS, size=n, p=PESOS_TIPO)

    # consumo_kwh y cantidad_equipos: parametros dependientes del tipo,
    # se rellenan por grupo para respetar la distribucion de cada inmueble.
    consumo_kwh = np.empty(n, dtype=float)
    cantidad_equipos = np.empty(n, dtype=int)

    for tipo in TIPOS:
        mask = tipo_inmueble == tipo
        tam = int(mask.sum())
        if tam == 0:
            continue

        media, sigma = CONSUMO_PARAMS[tipo]
        consumo_kwh[mask] = _normal_truncada(RNG, media, sigma, tam, CONSUMO_MINIMO)

        # Poisson desplazada: lambda sobre (equipos - 1) y luego +1 => minimo 1
        lam = EQUIPOS_LAMBDA[tipo]
        cantidad_equipos[mask] = RNG.poisson(lam - EQUIPOS_MINIMO, size=tam) + EQUIPOS_MINIMO

    # uso_horario_pico: Bernoulli cuya probabilidad crece con el consumo.
    # Se estandariza el consumo (media 0, desv 1) y se pasa por una logistica,
    # asi la senal es global y comparable entre tipos de inmueble.
    consumo_z = (consumo_kwh - consumo_kwh.mean()) / consumo_kwh.std()
    prob_pico = _sigmoide(PICO_K * consumo_z)
    uso_horario_pico = RNG.random(n) < prob_pico

    # horas_alto_consumo: uniforme discreta 1-12; si hay pico se sesga hacia
    # arriba tomando el maximo de dos tiradas (empuja la masa a valores altos).
    tirada_a = RNG.integers(1, 13, size=n)
    tirada_b = RNG.integers(1, 13, size=n)
    horas_alto_consumo = np.where(uso_horario_pico, np.maximum(tirada_a, tirada_b), tirada_a)

    # Outliers: ~4% de registros con consumo extremo (multiplicador alto).
    # Son intencionales: le dan material al EDA del Bloque B y credibilidad al set.
    n_outliers = round(OUTLIERS_FRAC * n)
    idx_outliers = RNG.choice(n, size=n_outliers, replace=False)
    consumo_kwh[idx_outliers] *= RNG.uniform(*OUTLIER_FACTOR, size=n_outliers)

    df = pd.DataFrame({
        "consumo_kwh": np.round(consumo_kwh, 2),
        "uso_horario_pico": uso_horario_pico,
        "cantidad_equipos": cantidad_equipos,
        "tipo_inmueble": tipo_inmueble,
        "horas_alto_consumo": horas_alto_consumo.astype(int),
    })
    return df


def _verificar_balance(df: pd.DataFrame) -> None:
    """Avisa si alguna categoria queda por debajo del minimo del DoD."""
    balance = df["categoria"].value_counts(normalize=True) * 100
    print("\nBalance de clases (%):")
    print(balance.round(1).to_string())
    if balance.min() < BALANCE_MINIMO:
        print(
            f"\n⚠️  Alguna clase quedo por debajo de {BALANCE_MINIMO:.0f}% "
            "-> revisar umbrales en labeling_rules.py (coordinar con Bloque B)."
        )
    else:
        print(f"\n✅ Todas las clases >= {BALANCE_MINIMO:.0f}%")


def main():
    df = generar_registros()
    df = etiquetar(df)  # agrega la columna 'categoria' (Bloque B define la formula)
    df.to_csv(RUTA_CSV, index=False)

    print(f"Dataset generado: {len(df)} registros -> {RUTA_CSV}")
    print(df.head())
    print("\nConsumo medio por tipo:")
    print(df.groupby("tipo_inmueble")["consumo_kwh"].mean().round(1).to_string())
    print("\nTasa de horario pico segun nivel de consumo (debe subir con el consumo):")
    tercios = pd.qcut(df["consumo_kwh"], 3, labels=["bajo", "medio", "alto"])
    print(df.groupby(tercios, observed=True)["uso_horario_pico"].mean().round(2).to_string())
    print("\nHoras alto consumo: media con pico vs sin pico:")
    print(df.groupby("uso_horario_pico")["horas_alto_consumo"].mean().round(1).to_string())
    _verificar_balance(df)


if __name__ == "__main__":
    main()
