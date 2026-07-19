# Dataset de consumo energético — EnergiAI (Bloque A)

Documentación del dataset simulado que alimenta el EDA (Bloque B) y el entrenamiento
del modelo (Bloque C).

- **Archivo:** [`data/dataset_energia.csv`](../data/dataset_energia.csv)
- **Generador:** [`src/generate_dataset.py`](../src/generate_dataset.py)
- **Etiquetado:** [`src/labeling_rules.py`](../src/labeling_rules.py) (fórmula del Bloque B)
- **Registros:** 4.000 · **Sin nulos** · **Seed fija:** `numpy.random.default_rng(seed=42)`

## Cómo regenerarlo

Desde la **raíz del repo** (las rutas del script son relativas a ella):

```bash
cd data-science
source .venv/bin/activate           # o: python -m venv .venv && pip install -r requirements.txt
cd ..
python data-science/src/generate_dataset.py
```

Gracias a la seed fija, cada corrida produce un CSV **idéntico byte a byte**
(mismo `md5`). Sin esto, el modelo del Bloque C daría métricas distintas en cada
regeneración.

## Esquema (Contrato 3)

| Columna | Tipo | Valores | Cómo se simula |
|---|---|---|---|
| `consumo_kwh` | float (>0) | mín. 30 | Normal truncada **por tipo** de inmueble |
| `uso_horario_pico` | bool | true/false | Bernoulli **correlacionada con el consumo** |
| `cantidad_equipos` | int (≥1) | — | Poisson **por tipo**, desplazada a mínimo 1 |
| `tipo_inmueble` | str | Casa / Departamento / Local | Muestreo ponderado |
| `horas_alto_consumo` | int | 1–12 | Uniforme discreta, sesgada si hay pico |
| `categoria` | str | Eficiente / Moderado / Ineficiente | Etiqueta derivada (Bloque B) |

## Distribuciones usadas

### `tipo_inmueble` — muestreo ponderado
Casa **45%** · Departamento **40%** · Local **15%**. Refleja que la mayoría de los
usuarios residenciales viven en casas o departamentos, y los locales comerciales son
minoría pero con consumos más altos.

### `consumo_kwh` — normal truncada por tipo (mínimo 30 kWh)
| Tipo | μ (media) | σ (desv.) |
|---|---|---|
| Casa | 350 | 120 |
| Departamento | 220 | 80 |
| Local | 600 | 200 |

El truncado se hace **re-muestreando** los valores por debajo de 30 kWh (no con
recorte/clip) para no acumular masa artificial en el piso y conservar una forma
realista.

### `cantidad_equipos` — Poisson por tipo (mínimo 1)
λ = **9** (Casa) · **6** (Departamento) · **15** (Local). El local comercial
concentra más equipos; el departamento, menos.

### `uso_horario_pico` — Bernoulli correlacionada con el consumo
La probabilidad de usar horario pico **crece con el consumo**: se estandariza
`consumo_kwh` (media 0, desv. 1) y se pasa por una función logística
(`p = sigmoide(1.2 · z)`). Esto es deliberado: si el pico fuera aleatorio puro, el
modelo no encontraría señal. Verificación por tercios de consumo:

| Nivel de consumo | Tasa de pico |
|---|---|
| bajo | 0.25 |
| medio | 0.46 |
| alto | 0.73 |

### `horas_alto_consumo` — uniforme discreta 1–12, sesgada por pico
Sin pico se toma una tirada uniforme 1–12; con pico se toma el **máximo de dos
tiradas**, lo que empuja la masa hacia valores altos. Media observada: **6.7 sin
pico** vs **8.5 con pico**.

### Outliers — ~4% de consumos extremos (intencionales)
Se selecciona el 4% de los registros y se multiplica su `consumo_kwh` por un factor
uniforme entre **2.5 y 4.0**. Son a propósito: le dan material al análisis de
outliers del Bloque B (detección por IQR) y hacen el dataset más creíble. Llevan el
consumo máximo hasta ~3.700 kWh frente a los ~600 kWh normales.

## Estadísticas del dataset generado

| Métrica | consumo_kwh | cantidad_equipos | horas_alto_consumo |
|---|---|---|---|
| media | 369.1 | 8.8 | 7.5 |
| desv. | 253.9 | 4.1 | 3.3 |
| mín | 32.0 | 1 | 1 |
| mediana | 310.1 | 8 | 8 |
| máx | 3734.2 | 30 | 12 |

## Balance de clases

Cumple el DoD del Bloque A (**ninguna clase < 20%**):

| Categoría | Registros | % |
|---|---|---|
| Eficiente | 1.568 | 39.2% |
| Moderado | 1.456 | 36.4% |
| Ineficiente | 976 | 24.4% |

El etiquetado no se fuerza: si el balance quedara fuera de rango, se ajustan los
**umbrales de la fórmula** en `labeling_rules.py` (coordinado con el Bloque B), no el
dataset. `generate_dataset.py` valida el balance automáticamente al final de cada corrida.

## Por qué es representativo

- Distribuciones **diferenciadas por tipo de inmueble** (una casa no consume como un
  local), en línea con patrones de consumo residencial y comercial.
- Correlaciones reales entre variables (consumo ↔ pico ↔ horas), no ruido aleatorio:
  el modelo tiene señal genuina que aprender.
- Presencia de outliers, como en datos reales de facturación.

## Limitaciones conocidas

- **Datos simulados**, no medidos: el brief lo permite explícitamente, pero no
  reemplazan mediciones reales de medidores.
- No modela **estacionalidad** (verano/invierno), **clima**, **antigüedad de los
  aparatos** ni **tarifas variables** por franja horaria.
- La etiqueta `categoria` se deriva de una **fórmula por reglas** (Bloque B): las
  clases son, por diseño, más separables que en datos reales, lo que facilita
  alcanzar buenas métricas en el Bloque C.
- Los parámetros (medias, σ, λ, pesos) son **supuestos razonables del equipo**, no
  calibrados contra un conjunto de datos real de la región.
