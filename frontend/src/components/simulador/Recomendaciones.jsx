import Card from "../ui/Card";

// Lista de recomendaciones del escenario simulado (vienen de la API / Bloque H).
export default function Recomendaciones({ items }) {
  if (!items || items.length === 0) return null;

  return (
    <Card>
      <h3 className="mb-3 text-sm font-medium text-slate-300">
        Recomendaciones para este escenario
      </h3>
      <ul className="space-y-2">
        {items.map((rec, i) => (
          <li key={i} className="flex gap-2.5 text-sm text-slate-300">
            <span className="mt-1.5 h-1.5 w-1.5 shrink-0 rounded-full bg-brand" />
            {rec}
          </li>
        ))}
      </ul>
    </Card>
  );
}
