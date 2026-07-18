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

RNG = np.random.default_rng(seed=42)
N_REGISTROS = 4000

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

    df = pd.DataFrame({
        "consumo_kwh": np.round(consumo_kwh, 2),
        "cantidad_equipos": cantidad_equipos,
        "tipo_inmueble": tipo_inmueble,
    })

    # TODO (commit 2): agregar las features restantes con senal para el modelo
    # - uso_horario_pico: Bernoulli correlacionada con el consumo
    # - horas_alto_consumo: uniforme discreta 1-12, sesgada si hay pico
    # - inyectar 3-5% de outliers (consumos extremos)
    return df


def main():
    df = generar_registros()
    # TODO (commit 3): etiquetar y exportar
    # from labeling_rules import etiquetar
    # df = etiquetar(df)  # agrega la columna 'categoria' (Bloque B define la formula)
    # df.to_csv("data-science/data/dataset_energia.csv", index=False)
    print(f"Registros generados: {len(df)}")
    print(df.head())
    print("\nConsumo medio por tipo:")
    print(df.groupby("tipo_inmueble")["consumo_kwh"].mean().round(1))


if __name__ == "__main__":
    main()
