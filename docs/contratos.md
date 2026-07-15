# Contratos del proyecto — EnergiAI

> Copia dentro del repo de los 5 contratos aprobados por el equipo en la
> First Meet. Ver la página de Notion "Bloque 0" para la versión completa
> con explicación. Si se modifica un contrato, se actualiza AQUÍ y se
> avisa al equipo — esta es la fuente de verdad técnica.

## Contrato 1 — API pública
`POST /analisis-energetico`

Entrada:
```json
{
  "consumo_kwh": 420,
  "uso_horario_pico": true,
  "cantidad_equipos": 10,
  "tipo_inmueble": "Casa",
  "horas_alto_consumo": 8
}
```
Salida:
```json
{
  "categoria": "Ineficiente",
  "probabilidad": 0.81,
  "recomendaciones": ["...", "...", "..."],
  "costo_estimado_mensual": 315.00
}
```
Consultas: `GET /analisis/{id}` · `GET /analisis?page=0&size=10`

## Contrato 2 — API interna (Backend ↔ ML Service)
`POST /predict`

Entrada: mismas 5 features · Salida: `{"categoria": "...", "probabilidad": 0.0}`

## Contrato 3 — Esquema del dataset
`consumo_kwh, uso_horario_pico, cantidad_equipos, tipo_inmueble, horas_alto_consumo, categoria`

## Contrato 4 — Esquema de base de datos
Tabla `analisis`: id + las columnas del Contrato 1 (entrada y resultado) + `creado_en`

## Contrato 5 — Estructura del repo
```
data-science/  ml-service/  backend/  frontend/  infra/
```

## Tarifa de referencia
`costo_estimado_mensual = consumo_kwh × 0.75`
