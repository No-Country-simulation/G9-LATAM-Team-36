import { Link } from "react-router-dom";
import Card from "../components/ui/Card";

// PERSONA 4 — Home / Landing (página de inicio)
// Construir aquí: hero con el nombre y propósito de EnergiAI, breve explicación
// de qué hace, y un botón que lleve a "/analizar".
// Componentes propios en:  src/components/home/
export default function Home() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-3xl font-bold">EnergiAI</h1>
        <p className="mt-1 text-slate-400">
          Analiza tu consumo eléctrico, conoce tu perfil de eficiencia y ahorra.
        </p>
      </div>
      <Card>
        <p className="text-slate-400">
          <b>Persona 4</b>: construir la landing aquí (hero + explicación + llamada a la acción).
          Componentes en <code className="text-brand">src/components/home/</code>.
        </p>
        <Link to="/analizar" className="btn-primary mt-4 inline-block">
          Analizar mi consumo
        </Link>
      </Card>
    </div>
  );
}
