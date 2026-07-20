# Criterios de los perfiles de eficiencia energética — EnergiAI (Bloque B)

Este documento **define y justifica** cómo EnergiAI clasifica un consumo como
*Eficiente*, *Moderado* o *Ineficiente*. Es el criterio que exige explícitamente el
brief: *"los equipos deberán definir y justificar los criterios para caracterizar los
diferentes perfiles de eficiencia energética."*

La fórmula vive, como única fuente de verdad, en
[`src/labeling_rules.py`](../src/labeling_rules.py); el Bloque A la usa para etiquetar
el dataset y el Bloque C entrena el modelo sobre esas etiquetas.

---

## 1. La idea: un índice relativo al consumo esperado

No tiene sentido comparar en crudo los kWh de una casa contra los de un local
comercial: consumen a escalas distintas por naturaleza. Por eso el criterio **no**
mira el consumo absoluto, sino **cuánto se desvía cada inmueble de lo que se espera
para su tipo**, penalizando además los hábitos que encarecen la factura (uso en
horario pico, muchas horas de alto consumo, muchos equipos por kWh).

Todo se resume en un **índice de eficiencia** donde **1.0 ≈ "consume lo esperado"**:

```
indice = 0.50 · (consumo_kwh / consumo_esperado[tipo_inmueble])
       + 0.20 · (consumo_kwh / cantidad_equipos) / kwh_por_equipo_ref
       + 0.20 · (horas_alto_consumo / 12)
       + 0.10 · uso_horario_pico
```

con:

| Constante | Valor | Fundamento |
|---|---|---|
| `consumo_esperado` | Casa 350 · Departamento 220 · Local 600 kWh/mes | Consumo mensual típico por tipo de inmueble; es el mismo centro usado para simular el dataset (Bloque A), así el índice ronda 1.0 para un consumo "normal". |
| `kwh_por_equipo_ref` | 35 kWh/mes | Consumo mensual de referencia de un electrodoméstico de uso medio; sirve para detectar equipos ineficientes o sobredimensionados. |

Cada término está **normalizado** (todos rondan 0–1.2 en uso normal), de modo que los
pesos son directamente comparables entre sí.

---

## 2. Justificación de cada peso

Los pesos reparten la "culpa" de la ineficiencia. Se eligieron para reflejar el orden
real de importancia de cada factor en la factura eléctrica residencial, y se validaron
midiendo cuánto aporta cada término al índice en el dataset real (columna "% del
índice", §4).

### `consumo_norm` — peso **0.50** (el más alto)
> *¿Cuánto consume frente a lo esperado para su tipo?*

Es el factor dominante porque **el consumo total es la causa directa del costo**: la
factura es, en esencia, kWh × tarifa. Un inmueble que consume muy por encima de lo
esperado para su categoría es, por definición, el candidato principal a ser
ineficiente. Le damos la mitad del peso porque ningún hábito puntual pesa tanto como
el volumen total de energía. En el dataset aporta el **55%** del índice, confirmando su
rol de driver principal.

### `consumo_por_equipo` — peso **0.20**
> *¿Cada equipo consume de más?*

Normaliza el consumo por la cantidad de equipos: distingue entre "consume mucho porque
tiene muchos aparatos" (razonable) y "consume mucho por pocos aparatos muy ineficientes"
(problema real). Un valor alto sugiere electrodomésticos viejos, sobredimensionados o
mal usados. Pesa menos que el consumo total porque es un matiz de *por qué* se consume,
no de *cuánto*. Aporta el **27%** del índice.

### `horas_alto_consumo` — peso **0.20**
> *¿Cuántas horas al día usa energía de forma intensiva?*

Muchas horas de alto consumo indican hábitos de uso intensivo y sostenido, que se
traducen directamente en más kWh facturados. Se normaliza sobre 12 horas (media
jornada). Recibe el mismo peso que `consumo_por_equipo` porque ambos describen *cómo*
se consume; aporta el **13%** del índice (menos en la práctica porque su valor medio es
moderado).

### `uso_horario_pico` — peso **0.10** (el más bajo)
> *¿Consume en las franjas caras?*

Es una penalización binaria por usar energía en horario pico, cuando la tarifa suele
ser más alta. Se le da el peso menor porque es un factor **agravante pero no
estructural**: encarece la factura y es el hábito más fácil de corregir (basta
reprogramar tareas), pero por sí solo no hace ineficiente a un consumo bajo. Aporta el
**5%** del índice. *Nota: es deliberadamente el hábito más accionable, y por eso el
Bloque H lo prioriza en las recomendaciones.*

**Regla de diseño:** consumo (volumen) > eficiencia por equipo ≈ intensidad horaria >
horario pico. El reparto 0.50 / 0.20 / 0.20 / 0.10 codifica ese orden.

---

## 3. Justificación de los umbrales: `< 0.80` · `0.80–1.05` · `> 1.05`

Como el índice se centra en 1.0 = "consume lo esperado", los umbrales definen cuánto
hay que desviarse de ese centro para cambiar de perfil:

| Perfil | Índice | Lectura |
|---|---|---|
| **Eficiente** | < 0.80 | Consume **al menos ~20% por debajo** de lo esperado: buenos hábitos. |
| **Moderado** | 0.80 – 1.05 | Consume **en torno a lo esperado**: banda de normalidad alrededor de 1.0. |
| **Ineficiente** | > 1.05 | Consume **por encima de lo esperado** con hábitos que lo agravan. |

**¿Por qué 0.80 y 1.05, y no los 0.85 / 1.15 iniciales?** Los umbrales de partida se
**calibraron contra el dataset real** con dos objetivos:

1. **Coherencia con el brief.** El ejemplo canónico del enunciado —
   `420 kWh · Casa · pico · 10 equipos · 8 h` — debe clasificarse como **Ineficiente**
   (así aparece en el README, los contratos y el DoD del Bloque C). Su índice es
   **1.073**; con el umbral inicial de 1.15 caía erróneamente en *Moderado*. Bajar el
   umbral superior a **1.05** lo coloca correctamente en *Ineficiente*.

2. **Balance de clases para el modelo.** Un dataset desbalanceado degrada el F1 macro
   del Bloque C. Los umbrales 0.80 / 1.05 dejan las tres clases casi uniformes
   (§4), lo que da al modelo la mejor base posible para aprender las tres por igual.

El ajuste **no modifica la simulación** del Bloque A (las features y la seed son
idénticas): solo recalcula la etiqueta de las filas cercanas a la frontera.

---

## 4. Validación empírica (dataset de 4.000 registros)

Todo lo anterior se sostiene con los datos generados:

**Balance de clases** — objetivo del DoD (ninguna < 20%), holgadamente cumplido:

| Categoría | Registros | % |
|---|---|---|
| Ineficiente | 1.399 | 35.0% |
| Eficiente | 1.313 | 32.8% |
| Moderado | 1.288 | 32.2% |

**Contribución media de cada término** (confirma el orden de los pesos):

| Término | Peso | % del índice |
|---|---|---|
| consumo_norm | 0.50 | 55.2% |
| consumo_por_equipo | 0.20 | 27.4% |
| horas_alto_consumo | 0.20 | 12.6% |
| uso_horario_pico | 0.10 | 4.8% |

**Separación por umbrales** (cada clase queda limpiamente en su banda):

| Categoría | índice mín | media | máx |
|---|---|---|---|
| Eficiente | 0.105 | 0.611 | 0.800 |
| Moderado | 0.800 | 0.924 | 1.050 |
| Ineficiente | 1.050 | 1.415 | 5.597 |

**Correlación de cada feature con el índice** (todas con el signo esperado):
`consumo_kwh` 0.75 · `uso_horario_pico` 0.30 · `horas_alto_consumo` 0.19 ·
`cantidad_equipos` −0.11 (a más equipos, menor consumo *por equipo*, luego índice algo
menor).

---

## 5. Limitaciones conocidas del criterio

Ser explícitos con lo que el índice **no** captura es parte de la honestidad del
criterio:

- **Es un modelo por reglas, no una medición.** El umbral entre *Moderado* e
  *Ineficiente* es una decisión de negocio calibrada, no una verdad física; un perfil
  justo en la frontera (índice ≈ 1.05) podría razonablemente caer en cualquiera de las
  dos clases.
- **No considera el contexto externo:** clima/estación (un verano caluroso dispara el
  aire acondicionado sin que haya "mal hábito"), antigüedad de los aparatos, ni número
  de habitantes del hogar.
- **Tarifa plana.** Asume una penalización fija por horario pico; no modela tarifas por
  franja horaria reales, que varían por región y proveedor.
- **`consumo_esperado` es un supuesto del equipo**, no un valor calibrado contra datos
  reales de consumo de la región; ajustar esos valores movería las tres clases.
- **Correlaciones inducidas por diseño.** Como el dataset es simulado por estas mismas
  reglas, las clases son más separables que en datos reales; conviene tenerlo presente
  al interpretar las métricas (probablemente optimistas) del Bloque C.

---

## 6. Implicaciones para otros bloques

- **Bloque C (modelo):** entrena sobre estas etiquetas. Si el balance o el ejemplo del
  brief fallaran, el ajuste se hace **aquí**, en los umbrales, no forzando el dataset.
- **Bloque H (recomendaciones):** el orden de los pesos indica qué atacar primero. El
  horario pico, aun siendo el de menor peso, es el hábito **más accionable** y por eso
  encabeza las recomendaciones; le siguen la intensidad horaria y los equipos
  ineficientes.
