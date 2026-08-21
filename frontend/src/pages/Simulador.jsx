import SimuladorAhorro from "../components/simulador/SimuladorAhorro";

// PERSONA 3 — Comparador de escenarios (el diferencial del demo)
export default function Simulador() {
  return (
    <div className="space-y-6">
      <div>
        <div className="flex flex-wrap items-center gap-3">
          <h1 className="text-2xl font-bold">Comparador de escenarios</h1>
          <span className="rounded-full bg-brand/10 px-3 py-1 text-xs font-medium text-brand">
            No se guarda en el historial
          </span>
        </div>
        <p className="mt-1 text-slate-400">
          Compara un perfil de referencia con cambios hipotéticos en tus hábitos y consumo.
        </p>
      </div>
      <SimuladorAhorro />
    </div>
  );
}
