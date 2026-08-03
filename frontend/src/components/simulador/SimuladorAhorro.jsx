import { useState, useEffect } from "react";
import { analizarConsumo } from "../../api/client";
import Card from "../ui/Card";
import SliderControl from "./SliderControl";
import ResumenAhorro from "./ResumenAhorro";
import Recomendaciones from "./Recomendaciones";
import ComparativaGrafica from "./ComparativaGrafica";

// Perfil de partida (tu consumo actual). Los sliders simulan cambios sobre esto.
const BASE = {
  consumo_kwh: 420,
  cantidad_equipos: 10,
  horas_alto_consumo: 8,
  uso_horario_pico: true,
  tipo_inmueble: "Casa",
};

export default function SimuladorAhorro() {
  const [sim, setSim] = useState(BASE);
  const [base, setBase] = useState(null); // resultado del escenario base (fijo)
  const [resultado, setResultado] = useState(null); // resultado simulado

  // El escenario base se calcula una sola vez.
  useEffect(() => {
    analizarConsumo(BASE).then(setBase).catch(() => {});
  }, []);

  // El escenario simulado recalcula con debounce al mover los controles.
  useEffect(() => {
    const t = setTimeout(() => {
      analizarConsumo(sim).then(setResultado).catch(() => {});
    }, 400);
    return () => clearTimeout(t);
  }, [sim]);

  const set = (campo) => (valor) => setSim((s) => ({ ...s, [campo]: valor }));

  const costoBase = base?.costo_estimado_mensual ?? 0;
  const costoSim = resultado?.costo_estimado_mensual ?? 0;
  const ahorro = base && resultado ? Math.max(0, costoBase - costoSim) : 0;
  const porcentaje = costoBase > 0 ? Math.round((ahorro / costoBase) * 100) : 0;

  return (
    <div className="grid gap-6 lg:grid-cols-2">
      {/* Controles */}
      <Card>
        <h2 className="font-semibold">Ajusta tus hábitos</h2>
        <p className="mt-1 text-sm text-slate-400">
          Parte de un consumo típico y mueve los controles para simular cambios.
        </p>
        <div className="mt-6 space-y-6">
          <SliderControl
            label="Consumo mensual"
            value={sim.consumo_kwh}
            min={30}
            max={1000}
            step={10}
            unit=" kWh"
            onChange={set("consumo_kwh")}
          />
          <SliderControl
            label="Cantidad de equipos"
            value={sim.cantidad_equipos}
            min={1}
            max={20}
            onChange={set("cantidad_equipos")}
          />
          <SliderControl
            label="Horas de alto consumo"
            value={sim.horas_alto_consumo}
            min={0}
            max={24}
            unit=" h"
            onChange={set("horas_alto_consumo")}
          />
          <Toggle
            checked={sim.uso_horario_pico}
            onChange={set("uso_horario_pico")}
            label="Uso en horario pico"
          />
        </div>
      </Card>

      {/* Resultados */}
      <div className="space-y-6">
        <ResumenAhorro
          base={base}
          simulado={resultado}
          ahorro={ahorro}
          porcentaje={porcentaje}
        />

        <Card>
          <h3 className="mb-3 text-sm font-medium text-slate-300">Comparación de costo</h3>
          {base && resultado ? (
            <ComparativaGrafica actual={costoBase} simulado={costoSim} />
          ) : (
            <p className="text-sm text-slate-500">Calculando…</p>
          )}
        </Card>

        <Recomendaciones items={resultado?.recomendaciones} />
      </div>
    </div>
  );
}

// Toggle accesible (usa transform para animar, sin reflow).
function Toggle({ checked, onChange, label }) {
  return (
    <div className="flex items-center justify-between">
      <span className="text-sm font-medium text-slate-300">{label}</span>
      <button
        type="button"
        role="switch"
        aria-checked={checked}
        aria-label={label}
        onClick={() => onChange(!checked)}
        className={`relative h-6 w-11 rounded-full transition-colors ${
          checked ? "bg-brand" : "bg-slate-700"
        }`}
      >
        <span
          className={`absolute left-0.5 top-0.5 h-5 w-5 rounded-full bg-white transition-transform ${
            checked ? "translate-x-5" : "translate-x-0"
          }`}
        />
      </button>
    </div>
  );
}
