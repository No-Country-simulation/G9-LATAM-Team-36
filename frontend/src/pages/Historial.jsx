import Card from "../components/ui/Card";

// PERSONA 2 — Vista de Historial
// Construir aquí: TablaHistorial (paginada) con badge de categoría por fila.
// Componentes propios en:  src/components/historial/
// API:  obtenerHistorial(page)  de  src/api/client.js
export default function Historial() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold">Historial de análisis</h1>
        <p className="mt-1 text-slate-400">
          Revisa los análisis realizados y su evolución.
        </p>
      </div>
      <Card>
        <p className="text-slate-400">
          <b>Persona 2</b>: construir la tabla de historial aquí.
          Componentes en <code className="text-brand">src/components/historial/</code>.
        </p>
      </Card>
    </div>
  );
}
