import Card from "../components/ui/Card";
import { useState, useEffect } from "react";
import { obtenerHistorial } from "../api/client";
import Badge from "../components/ui/Badge";
import { formatoMoneda, estiloDe } from "../lib/categoria";
import TablaHistorial from "../components/historial/TablaHistorial.jsx";
import Paginacion from "../components/historial/Paginacion.jsx";

// PERSONA 2 — Vista de Historial
// Construir aquí: TablaHistorial (paginada) con badge de categoría por fila.
// Componentes propios en:  src/components/historial/
// API:  obtenerHistorial(page)  de  src/api/client.js
export default function Historial() {

    const [page, setPage] = useState(0);
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const cargarHistorial=async (pagina) => {
        setLoading(true);
        setError(null);
        try {
            const resultado = await obtenerHistorial(pagina);
            setData(resultado);
        } catch (error) {
            setError("No se pudo cargar el historial");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        cargarHistorial(page);
    }, [page]);

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Historial de análisis</h1>
        <p className="mt-1 text-slate-400">
          Revisa los análisis realizados y su evolución.
        </p>
      </div>
      <Card>
          {loading && <p className="text-slate-400">Cargando historial...</p>}
          {error && <p className="text-red-400">{error}</p>}
          {!loading && !error && data && (
              <>
                  <TablaHistorial data={data.content} />
                  <Paginacion
                      page={page}
                      totalPages={data.totalPages}
                      onPageChange={setPage}
                  />
              </>
          )}
      </Card>
    </div>
  );
}
