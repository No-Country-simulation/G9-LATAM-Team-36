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


def generar_registros(n: int = N_REGISTROS) -> pd.DataFrame:
    # TODO (Bloque A): implementar la simulación por tipo de inmueble.
    # Ver la guía completa del Bloque A en el checklist de Notion:
    # - tipo_inmueble: muestreo ponderado (Casa 45%, Depto 40%, Local 15%)
    # - consumo_kwh: normal truncada por tipo
    # - cantidad_equipos: Poisson por tipo
    # - uso_horario_pico: Bernoulli correlacionada con consumo
    # - horas_alto_consumo: uniforme discreta, sesgada si hay pico
    raise NotImplementedError("Bloque A: implementar generación de registros")


def main():
    df = generar_registros()
    # from labeling_rules import etiquetar
    # df = etiquetar(df)  # agrega la columna 'categoria' (Bloque B define la fórmula)
    df.to_csv("data-science/data/dataset_energia.csv", index=False)
    print(f"Dataset generado: {len(df)} registros")


if __name__ == "__main__":
    main()
