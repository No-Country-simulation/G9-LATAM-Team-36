// Fuente de verdad del "semáforo" de eficiencia.
// Úsalo en TODAS las vistas para que los colores de categoría sean idénticos.

export const CATEGORIAS = ["Eficiente", "Moderado", "Ineficiente"];

export const ESTILO_CATEGORIA = {
  Eficiente: {
    label: "Eficiente",
    text: "text-emerald-400",
    bg: "bg-emerald-500/10",
    dot: "bg-emerald-500",
    hex: "#22c55e",
  },
  Moderado: {
    label: "Moderado",
    text: "text-amber-400",
    bg: "bg-amber-500/10",
    dot: "bg-amber-500",
    hex: "#eab308",
  },
  Ineficiente: {
    label: "Ineficiente",
    text: "text-red-400",
    bg: "bg-red-500/10",
    dot: "bg-red-500",
    hex: "#ef4444",
  },
};

export function estiloDe(categoria) {
  return (
    ESTILO_CATEGORIA[categoria] ?? {
      label: categoria ?? "—",
      text: "text-slate-400",
      bg: "bg-slate-500/10",
      dot: "bg-slate-500",
      hex: "#64748b",
    }
  );
}

// Formatea un número como moneda (para el costo estimado)
export function formatoMoneda(valor) {
  return new Intl.NumberFormat("es-MX", {
    style: "currency",
    currency: "USD",
  }).format(valor ?? 0);
}
