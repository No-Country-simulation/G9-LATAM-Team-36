# Servicio ML — EnergiAI (Bloque D)

Microservicio FastAPI que expone el modelo entrenado (Bloque C) como un endpoint de
predicción. Es el puente entre el modelo de Python y el Backend en Java (Contrato 2).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/predict` | Clasifica un perfil de consumo (Contrato 2). |
| `GET` | `/health` | Estado del servicio y del modelo cargado (versión, fuente, si es dummy). |
| `GET` | `/docs` | Documentación interactiva (Swagger UI, automática de FastAPI). |

**Ejemplo `POST /predict`:**
```json
// entrada
{"consumo_kwh": 420, "uso_horario_pico": true, "cantidad_equipos": 10,
 "tipo_inmueble": "Casa", "horas_alto_consumo": 8}
// salida (Contrato 2)
{"categoria": "Ineficiente", "probabilidad": 0.97}
```

## Cómo correrlo

### Local (con el modelo real)
Desde la **raíz del repo** (así encuentra `data-science/models/modelo.joblib`):
```bash
cd ml-service
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
cd ..
uvicorn app.main:app --reload --app-dir ml-service --port 8000
```

### Docker
```bash
cd ml-service
docker build -t energiai-ml-service .
docker run -d -p 8000:8000 --name ml energiai-ml-service
curl http://localhost:8000/health
docker rm -f ml
```
> En el contenedor el modelo real no está en el contexto de build, así que se usa
> `DummyModel` (`is_dummy: true`). El modelo real se sirve corriendo en local desde la
> raíz del monorepo, o vía OCI Object Storage cuando el Bloque J lo provisione.

### Tests
```bash
cd ml-service && source .venv/bin/activate
pytest -q
```

## Carga del modelo (variables de entorno)

Ver [`.env.example`](.env.example). Orden de resolución: `MODEL_PATH` → rutas locales →
descarga de OCI (`USE_LOCAL_MODEL=false`) → `DummyModel` como fallback.

## Troubleshooting

**`docker build` falla con `error getting credentials` / `docker-credential-secretservice ... not found`**

Docker está intentando usar un *credential helper* que no está instalado en tu sistema.
Ocurre en algunas instalaciones de Linux (p. ej. Fedora). Solución: edita
`~/.docker/config.json` y **elimina la clave `credsStore`** (o `credHelpers`) que apunte
a `docker-credential-secretservice` u otro helper inexistente. El archivo debe quedar
sin esa clave, por ejemplo:

```json
{
  "auths": {},
  "currentContext": "desktop-linux"
}
```

Tras guardar, `docker build` hará el *pull* anónimo de las imágenes públicas sin invocar
el helper. (Nota: es un ajuste de tu máquina, no del proyecto.)
