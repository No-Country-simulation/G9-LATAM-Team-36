import { useState } from "react";
import Card from "../components/ui/Card";
import FormularioConsumo from "../components/analisis/FormularioConsumo";
import ResultadoCard from "../components/analisis/ResultadoCard";
import GaugeEficiencia from "../components/analisis/GaugeEficiencia";
import ListaRecomendaciones from "../components/analisis/ListaRecomendaciones";
import { analizarConsumo } from "../api/client";

export default function Analisis() {
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  async function manejarAnalizar(datos) {
    setLoading(true);
    setError(null);
    try {
      const respuesta = await analizarConsumo(datos);
      setResultado(respuesta);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Analisis de consumo</h1>
        <p className="mt-1 text-slate-400">
          Ingresa los datos de consumo y obten tu perfil de eficiencia.
        </p>
      </div>

      <div className="grid gap-6 md:grid-cols-2 items-start">
        <Card>
          <FormularioConsumo onAnalizar={manejarAnalizar} loading={loading} />
        </Card>

        <div className="space-y-6">
          <Card>
            {error && <p className="text-red-400">{error}</p>}
            {!error && !resultado && !loading && (
              <p className="text-slate-400">Completa el formulario para ver tu resultado.</p>
            )}
            {resultado && (
              <>
                <GaugeEficiencia categoria={resultado.categoria} probabilidad={resultado.probabilidad} />
                <ResultadoCard resultado={resultado} />
              </>
            )}
          </Card>

          {resultado && <ListaRecomendaciones recomendaciones={resultado.recomendaciones} />}
        </div>
      </div>
    </div>
  );
}