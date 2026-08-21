import SimuladorAhorro from "../components/simulador/SimuladorAhorro";

// PERSONA 3 — Vista de Simulador de ahorro (el diferencial del demo)
export default function Simulador() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Simulador de ahorro</h1>
        <p className="mt-1 text-slate-400">
          Ajusta tus hábitos de consumo y descubre cuánto podrías ahorrar y cómo mejora tu eficiencia.
        </p>
      </div>
      <SimuladorAhorro />
    </div>
  );
}
