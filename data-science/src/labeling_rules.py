"""
Bloque B — Fórmula del índice de eficiencia energética.

Esta es la ÚNICA fuente de verdad del etiquetado: el Bloque A la importa
para generar el dataset, y debe coincidir con lo documentado y justificado
en notebooks/01_eda_y_criterios.ipynb y docs/criterios_perfiles.md.
"""
import pandas as pd

CONSUMO_ESPERADO = {"Casa": 350, "Departamento": 220, "Local": 600}
KWH_POR_EQUIPO_REF = 35

# Umbrales calibrados con el dataset del Bloque A (ver docs/criterios_perfiles.md):
# dejan las clases casi uniformes (~33/32/35) y hacen que el ejemplo canonico del
# brief (420 kWh, Casa, pico, 10 equipos, 8h -> indice 1.073) caiga en Ineficiente.
UMBRAL_EFICIENTE = 0.80
UMBRAL_MODERADO = 1.05


def calcular_indice(row: pd.Series) -> float:
    # TODO (Bloque B): ajustar pesos y justificar en docs/criterios_perfiles.md
    consumo_norm = row["consumo_kwh"] / CONSUMO_ESPERADO[row["tipo_inmueble"]]
    consumo_por_equipo = (row["consumo_kwh"] / row["cantidad_equipos"]) / KWH_POR_EQUIPO_REF
    horas_norm = row["horas_alto_consumo"] / 12
    pico = 1.0 if row["uso_horario_pico"] else 0.0

    return (
        0.5 * consumo_norm
        + 0.2 * consumo_por_equipo
        + 0.2 * horas_norm
        + 0.1 * pico
    )


def clasificar(indice: float) -> str:
    if indice < UMBRAL_EFICIENTE:
        return "Eficiente"
    if indice < UMBRAL_MODERADO:
        return "Moderado"
    return "Ineficiente"


def etiquetar(df: pd.DataFrame) -> pd.DataFrame:
    df = df.copy()
    df["indice_eficiencia"] = df.apply(calcular_indice, axis=1)
    df["categoria"] = df["indice_eficiencia"].apply(clasificar)
    return df.drop(columns=["indice_eficiencia"])
