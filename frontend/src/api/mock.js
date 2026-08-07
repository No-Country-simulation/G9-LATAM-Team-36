// Bloque I — Respuestas simuladas según el Contrato 1.
// Se usan mientras VITE_USE_MOCK=true, sin necesidad del backend corriendo.

export async function analizarConsumoMock(datos) {
  await delay(400);
  const ineficiente = datos.consumo_kwh > 400 || datos.horas_alto_consumo > 8;
  const moderado = !ineficiente && datos.consumo_kwh > 250;

  const categoria = ineficiente ? "Ineficiente" : moderado ? "Moderado" : "Eficiente";
  const probabilidad = ineficiente ? 0.81 : moderado ? 0.65 : 0.8;

  return {
    categoria,
    probabilidad,
    recomendaciones: [
      "Reducir el uso de equipos durante horarios pico",
      "Evaluar aparatos con alto consumo energético",
      "Distribuir actividades de mayor consumo a lo largo del día",
    ],
    costo_estimado_mensual: Number((datos.consumo_kwh * 0.75).toFixed(2)),
  };
}

export async function obtenerHistorialMock() {
  await delay(300);
  return {
    content: [
      { id: 1, categoria: "Ineficiente", consumo_kwh: 420, tipo_inmueble: "CASA", costo_estimado_mensual: 315.00, creado_en: new Date().toISOString() },
      { id: 2, categoria: "Moderado", consumo_kwh: 280, tipo_inmueble: "DEPARTAMENTO", costo_estimado_mensual: 210.00, creado_en: new Date().toISOString() },
    ],
    totalElements: 2,
    totalPages: 1,
  };
}

function delay(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}
