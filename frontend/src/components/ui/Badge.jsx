import { estiloDe } from "../../lib/categoria";

// Badge de categoría con el semáforo de eficiencia (verde/ámbar/rojo).
// Uso: <Badge categoria="Ineficiente" />
export default function Badge({ categoria, className = "" }) {
  const e = estiloDe(categoria);
  return (
    <span
      className={`inline-flex items-center gap-1.5 rounded-full px-3 py-1 text-sm font-medium ${e.bg} ${e.text} ${className}`}
    >
      <span className={`h-2 w-2 rounded-full ${e.dot}`} />
      {e.label}
    </span>
  );
}
