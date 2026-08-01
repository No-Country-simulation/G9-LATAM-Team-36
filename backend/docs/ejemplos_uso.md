# Ejemplos de uso - API de Análisis Energético

Este documento presenta tres ejemplos de uso del endpoint `POST /analisis-energetico`, correspondientes a los perfiles **Ineficiente**, **Moderado** y **Eficiente**.

---

# Ejemplo 1 - Perfil Ineficiente

## Request

```json
{
  "consumo_kwh": 750,
  "uso_horario_pico": true,
  "cantidad_equipos": 18,
  "tipo_inmueble": "CASA",
  "horas_alto_consumo": 9
}
```

## Response

```json
{
  "categoria": "Ineficiente",
  "probabilidad": 0.81,
  "recomendaciones": [
    "Evita utilizar los equipos de mayor consumo durante el horario pico.",
    "Mejora el aislamiento de tu vivienda y revisa el estado de tus equipos de climatización.",
    "Revisa periódicamente qué equipos permanecen conectados sin utilizarse y desconéctalos cuando no los necesites.",
    "Distribuye las actividades de mayor consumo en diferentes momentos del día para evitar periodos prolongados de alta demanda energética."
  ],
  "costo_estimado_mensual": 562.50
}
```

---

# Ejemplo 2 - Perfil Moderado

## Request

```json
{
  "consumo_kwh": 350,
  "uso_horario_pico": false,
  "cantidad_equipos": 8,
  "tipo_inmueble": "DEPARTAMENTO",
  "horas_alto_consumo": 5
}
```

## Response

```json
{
  "categoria": "Moderado",
  "probabilidad": 0.65,
  "recomendaciones": [
    "Mantén escalonado el uso de tus equipos.",
    "Identifica oportunidades de ahorro revisando el consumo de tus equipos eléctricos."
  ],
  "costo_estimado_mensual": 262.50
}
```

---

# Ejemplo 3 - Perfil Eficiente

## Request

```json
{
  "consumo_kwh": 200,
  "uso_horario_pico": false,
  "cantidad_equipos": 5,
  "tipo_inmueble": "CASA",
  "horas_alto_consumo": 3
}
```

## Response

```json
{
  "categoria": "Eficiente",
  "probabilidad": 0.8,
  "recomendaciones": [
    "Mantén revisiones periódicas de tus instalaciones eléctricas.",
    "Continúa registrando tu consumo mensual para identificar variaciones y conservar un uso eficiente de la energía."
  ],
  "costo_estimado_mensual": 150.00
}
```