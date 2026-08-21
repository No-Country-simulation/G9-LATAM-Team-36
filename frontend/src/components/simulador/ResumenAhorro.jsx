import Badge from "../ui/Badge";
import { formatoMoneda } from "../../lib/categoria";

// Tarjeta principal: transición de categoría y balance mensual con ahorro o sobrecosto.
export default function ResumenAhorro({ base, simulado, balance, porcentaje, actualizando }) {
  const cargando = !base || !simulado;
  const esAhorro = balance > 0.005;
  const esSobrecosto = balance < -0.005;
  const titulo = esAhorro
    ? "Ahorro mensual estimado"
    : esSobrecosto
      ? "Sobrecosto mensual estimado"
      : "Balance mensual estimado";
  const tono = esAhorro
    ? "border-emerald-500/40 bg-emerald-500/5 text-emerald-400"
    : esSobrecosto
      ? "border-red-500/40 bg-red-500/5 text-red-400"
      : "border-slate-700 bg-slate-900/60 text-slate-200";

  return (
    <div className={`rounded-2xl border p-6 text-center ${tono}`} aria-live="polite">
      {base && simulado && (
        <div className="flex items-center justify-center gap-3">
          <Badge categoria={base.categoria} />
          <span className="text-lg text-slate-500" aria-label="cambia a">
            &rarr;
          </span>
          <Badge categoria={simulado.categoria} />
        </div>
      )}

      <p className="mt-5 text-sm text-slate-400">{titulo}</p>
      <p className="mt-1 text-4xl font-bold tabular-nums">
        {cargando ? "—" : formatoMoneda(Math.abs(balance))}
      </p>

      {!cargando && esAhorro && (
        <p className="mt-1 text-sm font-medium tabular-nums">
          {porcentaje}% menos que el escenario actual
        </p>
      )}
      {!cargando && esSobrecosto && (
        <p className="mt-1 text-sm font-medium tabular-nums">
          {porcentaje}% más que el escenario actual
        </p>
      )}
      {!cargando && !esAhorro && !esSobrecosto && (
        <p className="mt-1 text-xs text-slate-500">Sin cambios en el costo mensual</p>
      )}
      {actualizando && <p className="mt-2 text-xs text-slate-500">Actualizando escenario…</p>}
    </div>
  );
}
