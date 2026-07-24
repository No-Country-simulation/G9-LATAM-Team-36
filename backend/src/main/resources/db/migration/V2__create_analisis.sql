CREATE TABLE IF NOT EXISTS analisis (
                          id                    BIGSERIAL PRIMARY KEY,
                          consumo_kwh           DOUBLE PRECISION,
                          uso_horario_pico      BOOLEAN,
                          cantidad_equipos      INTEGER,
                          tipo_inmueble         VARCHAR(50),
                          horas_alto_consumo    INTEGER,
                          categoria             VARCHAR(50),
                          probabilidad          NUMERIC(5,2),
                          costo_estimado_mensual NUMERIC(10,2),
                          recomendaciones       JSONB,
                          creado_en             TIMESTAMP
);