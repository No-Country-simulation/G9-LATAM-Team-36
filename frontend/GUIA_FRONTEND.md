# Guía del Frontend — EnergiAI (Bloque I)

Cómo trabajamos **3 personas en paralelo** sin pisarnos, manteniendo un estilo consistente.

## Cómo correrlo

```bash
cd frontend
npm install        # solo la primera vez
npm run dev        # abre http://localhost:5173 (con datos de prueba/mock)
```

Por defecto usa **mocks** (`VITE_USE_MOCK=true`), así que **no necesitas el backend**.
Para conectar a la API real: copia `.env.example` a `.env` y pon `VITE_USE_MOCK=false`.

---

## Reparto del trabajo (cada quien SU carpeta)

| Persona | Vista | Su página | Su carpeta de componentes |
|---|---|---|---|
| **Persona 1** | Análisis (formulario + resultado) | `src/pages/Analisis.jsx` | `src/components/analisis/` |
| **Persona 2** | Historial | `src/pages/Historial.jsx` | `src/components/historial/` |
| **Persona 3** | Simulador de ahorro | `src/pages/Simulador.jsx` | `src/components/simulador/` |

**Regla de oro:** cada quien crea/edita archivos **solo dentro de su carpeta** y su página.
Así nunca hay conflictos de Git.

### Archivos compartidos — NO tocar sin avisar
Estos ya están listos; si necesitas cambiarlos, avisa por el grupo primero:
- `src/App.jsx` (router) · `src/components/layout/` (navbar/layout)
- `tailwind.config.js` · `src/index.css` · `index.html`
- `src/api/` (client/mock) · `src/lib/` (helpers compartidos)
- `package.json` (si necesitas una librería nueva, **coordínalo** — un solo `npm install` para todos)

---

## Sistema de diseño (usar SIEMPRE esto)

### Tema
Modo **oscuro** como tema principal. Fondo `slate-950`, tarjetas `slate-900`, texto `slate-100` / `slate-400` (secundario). Tipografía **Inter** (ya cargada).

### Paleta
| Uso | Token Tailwind | Hex |
|---|---|---|
| Marca / acción principal | `brand` | `#10b981` |
| Eficiente | `eficiente` | `#22c55e` |
| Moderado | `moderado` | `#eab308` |
| Ineficiente | `ineficiente` | `#ef4444` |
| Fondo / superficies | `slate-950` / `slate-900` | — |
| Texto principal / secundario | `slate-100` / `slate-400` | — |

> Para los **colores de categoría** NO uses hex sueltos: importa `estiloDe(categoria)` de
> `src/lib/categoria.js` (devuelve clases y hex coherentes). Así el semáforo es idéntico en
> todas las vistas.

### Componentes base (reutilízalos)
- `<Card>` — contenedor tipo panel · `src/components/ui/Card.jsx`
- `<Button variant="primary|ghost">` · `src/components/ui/Button.jsx`
- `<Badge categoria="Ineficiente" />` — badge con semáforo · `src/components/ui/Badge.jsx`

### Clases CSS reutilizables (en `index.css`)
- `.card` — panel · `.btn-primary` / `.btn-ghost` — botones
- `.field` — inputs/selects · `.label` — etiquetas de formulario

### Convenciones
- Espaciado con la escala de Tailwind (`gap-4`, `p-6`, `space-y-6`…). Bordes redondeados: `rounded-xl` / `rounded-2xl`.
- Componentes en **PascalCase** (`ResultadoCard.jsx`), un componente por archivo.
- Nada de estilos "a mano" con hex sueltos: usa los tokens y clases de arriba.

---

## Datos (contrato con la API)

Todo pasa por `src/api/client.js` (no llames `fetch` directo desde las vistas):
- `analizarConsumo(datos)` → `POST /analisis-energetico`
- `obtenerHistorial(page)` → `GET /analisis`

**Entrada** (`datos`): `consumo_kwh`, `uso_horario_pico`, `cantidad_equipos`, `tipo_inmueble` (`Casa`/`Departamento`/`Local`), `horas_alto_consumo`.
**Salida**: `categoria`, `probabilidad`, `recomendaciones[]`, `costo_estimado_mensual`.

Los mocks (`src/api/mock.js`) ya devuelven esta forma — trabaja contra ellos.

---

## Cómo construir cada vista

Cada quien construye componentes pequeños dentro de su carpeta y los une en su página.
Reutiliza siempre `Card`, `Button`, `Badge`, `estiloDe()` y las clases (`.field`, `.label`, `.card`).

### Persona 1 — Análisis (`pages/Analisis.jsx`)

**Objetivo:** un formulario con los 5 datos de consumo; al enviarlo, mostrar el resultado.

**Componentes a crear en `src/components/analisis/`:**
- `FormularioConsumo.jsx` — los 5 campos (usar `.field` y `.label`):
  - `consumo_kwh`: número · `tipo_inmueble`: select (`Casa`/`Departamento`/`Local`)
  - `cantidad_equipos`: número (mín. 1) · `horas_alto_consumo`: slider o número 0–24
  - `uso_horario_pico`: toggle/checkbox · botón "Analizar" (`<Button>`)
- `ResultadoCard.jsx` — dentro de un `<Card>`: `<Badge categoria={...} />`, la probabilidad como
  porcentaje, y el `costo_estimado_mensual` destacado (usar `formatoMoneda()`).
- `GaugeEficiencia.jsx` — `RadialBarChart` de Recharts con la probabilidad; color = `estiloDe(categoria).hex`.
- `ListaRecomendaciones.jsx` — lista de `recomendaciones[]`, cada una en su tarjeta.

**Estructura de la página:** estado para `datos`, `resultado`, `loading`, `error`.
Layout en 2 columnas (formulario | resultado), apilado en móvil. Al enviar:
`analizarConsumo(datos)` → guardar en `resultado`.

**Terminado cuando:** el form valida, llama a la API (mock), y muestra categoría con semáforo +
costo + gauge + recomendaciones, con estados de carga y error.

### Persona 2 — Historial (`pages/Historial.jsx`)

**Objetivo:** una tabla paginada con los análisis previos.

**Componentes a crear en `src/components/historial/`:**
- `TablaHistorial.jsx` — tabla con columnas: fecha (`creado_en`), consumo, `tipo_inmueble`,
  categoría (`<Badge>`), costo. Filas con hover.
- `Paginacion.jsx` (opcional) — botones anterior/siguiente.

**Estructura de la página:** estado para `page`, `datos`, `loading`. Al montar y al cambiar de
página: `obtenerHistorial(page)`. La respuesta trae `{ content: [...], totalElements }`.

**Terminado cuando:** la tabla muestra los datos del mock con badge por fila, hay paginación,
y estados de "cargando" y "sin datos".

### Persona 3 — Simulador de ahorro (`pages/Simulador.jsx`)

**Objetivo (el diferencial del demo):** sliders que ajustan hábitos y recalculan en vivo,
mostrando escenario actual vs simulado y cuánto se ahorra.

**Componentes a crear en `src/components/simulador/`:**
- `SimuladorAhorro.jsx` — sliders para `horas_alto_consumo` y `cantidad_equipos`, toggle de
  `uso_horario_pico`, partiendo de un escenario base.
- `ComparativaEscenarios.jsx` — lado a lado: actual vs simulado (categoría + costo), con el
  ahorro en `$` resaltado (usar `formatoMoneda()`).
- gráfica de barras (Recharts) comparando el costo actual vs el simulado.

**Estructura de la página:** estado para `escenarioBase` y `escenarioSimulado`. Al mover un
slider, con **debounce (~400 ms)** llamar `analizarConsumo(simulado)` y actualizar la comparativa.

**Terminado cuando:** los sliders recalculan en vivo (con debounce), se ve la diferencia de
costo/categoría entre actual y simulado, y hay una gráfica comparativa.

---

## Flujo de Git

1. La base ya está en `dev` (esta guía + layout + router + UI).
2. Cada persona crea su rama desde `dev`:
   - `feature/I-analisis` · `feature/I-historial` · `feature/I-simulador`
3. Trabaja **solo en tu carpeta**, haz commits pequeños, y abre PR a `dev`.
4. Como cada quien toca archivos distintos, los PRs no chocan entre sí.
