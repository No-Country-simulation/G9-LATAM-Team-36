# ⚡ EnergiAI — Inteligencia para el Consumo Energético

Hackathon ONE G9 – Alura + Oracle | No Country · Team 36

Solución que analiza patrones de consumo eléctrico, clasifica el perfil energético
(Eficiente / Moderado / Ineficiente), genera recomendaciones de ahorro y estima el
costo mensual, expuesta como API REST y desplegada sobre OCI.

## 📁 Estructura del repo

| Carpeta | Contenido | Bloques dueños |
|---|---|---|
| `data-science/` | Dataset, EDA, entrenamiento del modelo | A, B, C |
| `ml-service/` | Microservicio FastAPI que sirve el modelo | D |
| `backend/` | API REST en Spring Boot | E, F, G, H |
| `frontend/` | Interfaz web en React | I |
| `infra/` | Docker Compose, scripts de despliegue, notas de OCI | J |

Ver el checklist completo por bloques y la página de Arquitectura OCI en Notion
para el detalle de cada uno.

## 🚀 Cómo correr todo en local

```bash
cp .env.example .env        # completar variables (ver infra/oci/notas.md)
docker compose up -d --build
```

- Frontend: http://localhost:80
- Backend / Swagger: http://localhost:8080/swagger-ui.html
- ML service / docs: http://localhost:8000/docs

## 🧩 Contratos (ver detalle en Notion → Bloque 0)

- **POST /analisis-energetico** — endpoint principal, contrato de entrada/salida en `docs/contratos.md` (o página de Notion)
- **POST /predict** (interno) — contrato Backend ↔ ML service
- Tarifa de referencia: **$0.75 / kWh**

## 🏗️ Arquitectura

Ver página de Notion **"Arquitectura OCI — EnergiAI"** para el diagrama completo y
la justificación de los servicios de OCI usados (Object Storage + OCI Compute).

## 👥 Equipo — G9-LATAM-Team 36

| Nombre | Bloque(s) |
|---|---|
| _______ | _______ |
| _______ | _______ |
| _______ | _______ |
