import { useState, useEffect, useRef } from "react";
import { analizarConsumo } from "../../api/client";
import Card from "../ui/Card";
import Button from "../ui/Button";
import SliderControl from "./SliderControl";
import ResumenAhorro from "./ResumenAhorro";
import Recomendaciones from "./Recomendaciones";
import ComparativaGrafica from "./ComparativaGrafica";
import ComparativaEscenarios from "./ComparativaEscenarios";
import { formatoMoneda } from "../../lib/categoria";

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
  const [actualizando, setActualizando] = useState(false);
  const [error, setError] = useState(null);
  const solicitudActual = useRef(0);

  // El escenario base se calcula una sola vez.
  useEffect(() => {
    analizarConsumo(BASE, { persistir: false })
      .then(setBase)
      .catch(() => setError("No pudimos calcular el escenario de referencia."));
  }, []);

  // El escenario simulado recalcula con debounce al mover los controles.
  useEffect(() => {
    const t = setTimeout(() => {
      const idSolicitud = ++solicitudActual.current;
      setActualizando(true);
      setError(null);
      analizarConsumo(sim, { persistir: false })
        .then((respuesta) => {
          if (idSolicitud === solicitudActual.current) setResultado(respuesta);
        })
        .catch(() => {
          if (idSolicitud === solicitudActual.current) {
            setError("No pudimos actualizar la simulación. Intenta nuevamente.");
          }
        })
        .finally(() => {
          if (idSolicitud === solicitudActual.current) setActualizando(false);
        });
    }, 400);
    return () => clearTimeout(t);
  }, [sim]);

  const set = (campo) => (valor) => setSim((s) => ({ ...s, [campo]: valor }));

  const costoBase = base?.costo_estimado_mensual ?? 0;
  const costoSim = resultado?.costo_estimado_mensual ?? 0;
  const balance = base && resultado ? costoBase - costoSim : 0;
  const porcentaje = costoBase > 0 ? Math.round((Math.abs(balance) / costoBase) * 100) : 0;
  const escenarioSinCambios = Object.keys(BASE).every((campo) => sim[campo] === BASE[campo]);

  function restablecer() {
    setSim({ ...BASE });
  }

  return (
    <div className="grid min-w-0 items-start gap-6 lg:grid-cols-2">
      {/* Controles */}
      <Card className="min-w-0">
        <div className="flex flex-wrap items-start justify-between gap-4">
          <div>
            <h2 className="font-semibold">Escenario simulado</h2>
            <p className="mt-1 text-sm text-slate-400">
              Ajusta valores hipotéticos y compáralos con la referencia actual.
            </p>
          </div>
          <Button
            variant="ghost"
            className="ml-auto"
            onClick={restablecer}
            disabled={escenarioSinCambios}
          >
            Restablecer
          </Button>
        </div>

        <div className="mt-5 rounded-xl border border-slate-700/80 bg-slate-950/60 p-4">
          <div className="flex flex-wrap items-center justify-between gap-2">
            <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">
              Escenario actual · referencia fija
            </p>
            <span className="text-sm font-semibold tabular-nums text-slate-200">
              {base ? formatoMoneda(costoBase) : "Calculando…"}
            </span>
          </div>
          <p className="mt-2 text-sm text-slate-300">
            Casa · 420 kWh · 10 equipos · 8 h intensivas · con horario pico
          </p>
        </div>

        <div className="mt-6 space-y-6">
          <SliderControl
            label="Consumo mensual"
            value={sim.consumo_kwh}
            min={30}
            max={3750}
            step={10}
            unit=" kWh"
            onChange={set("consumo_kwh")}
          />
          <SliderControl
            label="Cantidad de equipos"
            value={sim.cantidad_equipos}
            min={1}
            max={30}
            onChange={set("cantidad_equipos")}
          />
          <SliderControl
            label="Horas de alto consumo"
            value={sim.horas_alto_consumo}
            min={0}
            max={12}
            unit=" h"
            onChange={set("horas_alto_consumo")}
          />
          <Toggle
            checked={sim.uso_horario_pico}
            onChange={set("uso_horario_pico")}
            label="Uso en horario pico"
          />
        </div>

        <div className="mt-6 space-y-2 rounded-xl border border-sky-500/20 bg-sky-500/5 p-4 text-xs leading-relaxed text-slate-400">
          <p>
            <strong className="text-slate-200">Cómo se calcula:</strong> el costo mensual depende
            del consumo en kWh (tarifa de referencia: USD 0.75/kWh).
          </p>
          <p>
            Equipos, horas y horario pico influyen en la categoría y las recomendaciones, pero no
            cambian el costo si mantienes los mismos kWh.
          </p>
          <p className="font-medium text-brand">Esta simulación es temporal y no se guarda.</p>
        </div>

        {error && (
          <p className="mt-4 rounded-xl border border-red-500/30 bg-red-500/10 p-3 text-sm text-red-300">
            {error}
          </p>
        )}
      </Card>

      {/* Resultados */}
      <div className="min-w-0 space-y-6">
        <ResumenAhorro
          base={base}
          simulado={resultado}
          balance={balance}
          porcentaje={porcentaje}
          actualizando={actualizando}
        />

        <ComparativaEscenarios actual={BASE} simulado={sim} base={base} resultado={resultado} />

        <Card>
          <h3 className="mb-1 text-sm font-medium text-slate-300">Comparación de costo mensual</h3>
          <p className="mb-3 text-xs text-slate-500">Gris: actual · verde: ahorro · rojo: sobrecosto</p>
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
