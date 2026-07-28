import Card from "../components/ui/Card";

// PERSONA 3 — Vista de Simulador de ahorro (el diferencial del demo)
// Construir aquí: sliders que ajustan hábitos y muestran en vivo cómo cambia
// la eficiencia y el costo (escenario actual vs simulado). Gráficas con Recharts.
// Componentes propios en:  src/components/simulador/
// API:  reutiliza analizarConsumo(datos)  de  src/api/client.js
export default function Simulador() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Simulador de ahorro</h1>
        <p className="mt-1 text-slate-400">
          Ajusta tus hábitos y descubre cuánto podrías ahorrar.
        </p>
      </div>
      <Card>
        <p className="text-slate-400">
          <b>Persona 3</b>: construir el simulador y las gráficas aquí.
          Componentes en <code className="text-brand">src/components/simulador/</code>.
        </p>
      </Card>
    </div>
  );
}
