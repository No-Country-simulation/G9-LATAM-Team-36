import Card from "../components/ui/Card";

// PERSONA 1 — Vista de Análisis
// Construir aquí: FormularioConsumo + ResultadoCard + GaugeEficiencia + ListaRecomendaciones
// Componentes propios en:  src/components/analisis/
// API:  analizarConsumo(datos)  de  src/api/client.js
export default function Analisis() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Análisis de consumo</h1>
        <p className="mt-1 text-slate-400">
          Ingresa los datos de consumo y obtén tu perfil de eficiencia.
        </p>
      </div>
      <Card>
        <p className="text-slate-400">
          <b>Persona 1</b>: construir el formulario y la tarjeta de resultado aquí.
          Componentes en <code className="text-brand">src/components/analisis/</code>.
        </p>
      </Card>
    </div>
  );
}
