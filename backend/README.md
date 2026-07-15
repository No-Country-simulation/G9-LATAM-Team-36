# Backend — EnergiAI (Spring Boot)

Dueños: Bloques E (API core), F (validación/errores/docs), G (persistencia), H (recomendaciones)

## Paquetes y quién es dueño de cada uno

| Paquete | Dueño | Nota |
|---|---|---|
| `controller/` | E (AnalisisController) y G (ConsultaController) | Controllers separados, no se mezclan |
| `service/` | E (AnalisisService) | Orquestador principal |
| `service/impl/` | H (RecomendacionServiceImpl) | Implementa la interfaz que define E |
| `client/` | E | MlClient (interfaz), MlClientMock (dev), MlClientHttp (prod) |
| `dto/` | E, con anotaciones de F | Contrato 1 y 2 |
| `exception/` | F | Manejo global de errores |
| `repository/`, `model/` | G | Persistencia |

## Correr en local (perfil dev, con mock del ML)

```bash
cd backend
./mvnw spring-boot:run
```

Por defecto usa `MlClientMock` (perfil dev) — no necesitas el servicio Python corriendo.

Swagger UI: http://localhost:8080/swagger-ui.html
