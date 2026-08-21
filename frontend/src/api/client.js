// Bloque I — Única capa que conoce las URLs del backend.
// Conmuta entre mock y API real con VITE_USE_MOCK (ver .env.example).

import { analizarConsumoMock, obtenerHistorialMock } from "./mock";

const USE_MOCK = import.meta.env.VITE_USE_MOCK === "true";
const API_URL = import.meta.env.VITE_API_URL || "";

export async function analizarConsumo(datos, { persistir = true } = {}) {
  if (USE_MOCK) return analizarConsumoMock(datos);

  const query = persistir ? "" : "?persistir=false";
  const res = await fetch(`${API_URL}/analisis-energetico${query}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(datos),
  });
  if (!res.ok) {
    const error = await res.json().catch(() => ({}));
    throw new Error(error?.errores?.[0]?.mensaje || "Error al analizar el consumo");
  }
  return res.json();
}

export async function obtenerHistorial(page = 0) {
  if (USE_MOCK) return obtenerHistorialMock();

  const res = await fetch(`${API_URL}/analisis?page=${page}&size=10`);
  if (!res.ok) throw new Error("Error al obtener el historial");
  return res.json();
}
