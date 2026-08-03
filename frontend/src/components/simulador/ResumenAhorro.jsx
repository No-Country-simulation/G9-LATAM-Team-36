import Badge from "../ui/Badge";
import { formatoMoneda } from "../../lib/categoria";

// Tarjeta principal: transición de categoría (actual -> simulado) + ahorro ($ y %).
export default function ResumenAhorro({ base, simulado, ahorro, porcentaje }) {
  return (
    <div className="rounded-2xl border border-brand/40 bg-brand/5 p-6 text-center">
      {base && simulado && (
        <div className="flex items-center justify-center gap-3">
          <Badge categoria={base.categoria} />
          <span className="text-lg text-slate-500" aria-label="cambia a">
            &rarr;
          </span>
          <Badge categoria={simulado.categoria} />
        </div>
      )}

      <p className="mt-5 text-sm text-slate-400">Ahorro mensual estimado</p>
      <p className="mt-1 text-4xl font-bold tabular-nums text-brand">
        {formatoMoneda(ahorro)}
      </p>

      {porcentaje > 0 ? (
        <p className="mt-1 text-sm font-medium tabular-nums text-brand/80">
          {porcentaje}% menos que tu consumo actual
        </p>
      ) : (
        <p className="mt-1 text-xs text-slate-500">
          Mueve los controles para ver tu ahorro
        </p>
      )}
    </div>
  );
}
