<div align="center">

# ⚡ EnergiAI

### Inteligencia artificial para entender, clasificar y optimizar tu consumo eléctrico

[![Estado](https://img.shields.io/badge/estado-en%20desarrollo-yellow)]()
[![Licencia](https://img.shields.io/badge/licencia-MIT-blue)]()
[![Python](https://img.shields.io/badge/Python-3.11-3776AB?logo=python&logoColor=white)]()
[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6DB33F?logo=springboot&logoColor=white)]()
[![React](https://img.shields.io/badge/React-18-61DAFB?logo=react&logoColor=black)]()
[![OCI](https://img.shields.io/badge/Oracle%20Cloud-Infrastructure-F80000?logo=oracle&logoColor=white)]()

</div>

---

## 💡 ¿Qué es EnergiAI?

Muchas personas reciben facturas de electricidad elevadas sin entender realmente qué
hábitos de consumo las provocan. **EnergiAI** analiza los datos de consumo eléctrico
de una vivienda o pequeño establecimiento y responde tres preguntas clave:

- **¿Qué tan eficiente es mi consumo?** — clasificación automática en *Eficiente*, *Moderado* o *Ineficiente*, con un modelo de Machine Learning entrenado sobre patrones reales de uso.
- **¿Qué puedo hacer para mejorarlo?** — recomendaciones concretas y personalizadas según los hábitos detectados.
- **¿Cuánto me está costando?** — una estimación clara del gasto mensual asociado.

El objetivo es transformar datos crudos de consumo en información simple y accionable
que ayude a tomar mejores decisiones sobre el uso de la energía en el hogar o en el
negocio.

---

## ✨ Funcionalidades

- 🔍 **Clasificación de perfil energético** mediante un modelo de Machine Learning (Random Forest / Regresión Logística) entrenado con datos de consumo residencial.
- 💬 **Recomendaciones personalizadas** de ahorro, generadas según los hábitos específicos detectados en cada análisis.
- 💰 **Estimación de costo mensual**, calculada sobre una tarifa de referencia.
- 📊 **Historial de análisis**, para hacer seguimiento de la evolución del consumo a lo largo del tiempo.
- 🧪 **Simulador de escenarios** — permite explorar cómo cambiaría la eficiencia y el costo al ajustar hábitos de uso (horarios, cantidad de equipos, horas de uso intensivo).
- 📖 **API documentada** y lista para integrarse con otras aplicaciones o sistemas.

---

## 🖼️ Vista previa

> _Capturas de la interfaz disponibles próximamente._

---

## 🧱 Arquitectura

EnergiAI está compuesto por cuatro servicios independientes que trabajan en conjunto,
desplegados sobre **Oracle Cloud Infrastructure (OCI)**:

```
                    ┌───────────────────────┐
   Usuario  ───────►│   Interfaz web (React) │
                    └───────────┬───────────┘
                                │
                    ┌───────────▼───────────┐
                    │   API REST (Spring Boot) │──────► Base de datos
                    └───────────┬───────────┘         (historial de análisis)
                                │
                    ┌───────────▼───────────┐
                    │ Servicio de predicción  │◄────── Modelo entrenado
                    │      (FastAPI)          │        (Object Storage)
                    └───────────────────────┘
```

- **Interfaz web** — formulario de ingreso de datos, visualización de resultados y simulador interactivo.
- **API REST** — valida la información recibida, orquesta el análisis y calcula el costo estimado.
- **Servicio de predicción** — ejecuta el modelo de Machine Learning entrenado y devuelve la clasificación.
- **Infraestructura en la nube (OCI)** — aloja la aplicación con disponibilidad continua y almacena el modelo entrenado.

---

## 🛠️ Tecnologías utilizadas

| Área | Tecnología |
|---|---|
| Ciencia de datos | Python, Pandas, Scikit-Learn |
| Servicio de predicción | FastAPI |
| API principal | Java 21, Spring Boot 3 |
| Base de datos | PostgreSQL |
| Interfaz web | React, Vite, TailwindCSS |
| Infraestructura | Docker, Oracle Cloud Infrastructure (Object Storage + Compute) |

---

## 🚀 Cómo probarlo localmente

Se requiere tener **Docker** y **Docker Compose** instalados.

```bash
git clone https://github.com/No-Country-simulation/G9-LATAM-Team-36.git
cd G9-LATAM-Team-36
cp .env.example .env
docker compose up -d --build
```

Una vez levantado:

| Servicio | URL |
|---|---|
| Interfaz web | http://localhost |
| Documentación de la API | http://localhost:8080/swagger-ui.html |
| Documentación del servicio de predicción | http://localhost:8000/docs |

---

## 📡 Ejemplo de uso de la API

**Solicitud** — `POST /analisis-energetico`

```json
{
  "consumo_kwh": 420,
  "uso_horario_pico": true,
  "cantidad_equipos": 10,
  "tipo_inmueble": "Casa",
  "horas_alto_consumo": 8
}
```

**Respuesta**

```json
{
  "categoria": "Ineficiente",
  "probabilidad": 0.81,
  "recomendaciones": [
    "Reducir el uso de equipos durante horarios pico",
    "Evaluar aparatos con alto consumo energético",
    "Distribuir actividades de mayor consumo a lo largo del día"
  ],
  "costo_estimado_mensual": 315.00
}
```

La documentación interactiva completa está disponible en `/swagger-ui.html` una vez
que el proyecto está en ejecución.

---

## 📂 Estructura del repositorio

```
├── data-science/   Exploración de datos, entrenamiento y evaluación del modelo
├── ml-service/     Servicio de predicción (FastAPI)
├── backend/        API REST (Spring Boot)
├── frontend/       Interfaz web (React)
└── infra/          Configuración de despliegue y de infraestructura en la nube
```

---

## 📍 Estado del proyecto

Este proyecto se encuentra **en desarrollo activo** como parte de un programa de
formación práctica en tecnología (Hackathon ONE, en colaboración con Alura y
Oracle). Las funcionalidades descritas en este documento reflejan el alcance
planeado del producto mínimo viable.

---

## 📄 Licencia

Este proyecto se distribuye bajo la licencia MIT. Ver el archivo `LICENSE` para más
información.

---

<div align="center">

**EnergiAI** — Transformando datos de consumo en decisiones más conscientes.

</div>
