import Badge from "../ui/Badge";
import Card from "../ui/Card";

function diferenciaNumerica(actual, simulado, unidad = "") {
  const diferencia = simulado - actual;
  if (diferencia === 0) return "Sin cambios";
  const signo = diferencia > 0 ? "+" : "";
  return `${signo}${diferencia}${unidad}`;
}

export default function ComparativaEscenarios({ actual, simulado, base, resultado }) {
  const filas = [
    {
      etiqueta: "Consumo mensual",
      actual: `${actual.consumo_kwh} kWh`,
      simulado: `${simulado.consumo_kwh} kWh`,
      diferencia: diferenciaNumerica(actual.consumo_kwh, simulado.consumo_kwh, " kWh"),
    },
    {
      etiqueta: "Cantidad de equipos",
      actual: actual.cantidad_equipos,
      simulado: simulado.cantidad_equipos,
      diferencia: diferenciaNumerica(actual.cantidad_equipos, simulado.cantidad_equipos),
    },
    {
      etiqueta: "Horas intensivas",
      actual: `${actual.horas_alto_consumo} h`,
      simulado: `${simulado.horas_alto_consumo} h`,
      diferencia: diferenciaNumerica(actual.horas_alto_consumo, simulado.horas_alto_consumo, " h"),
    },
    {
      etiqueta: "Horario pico",
      actual: actual.uso_horario_pico ? "Sí" : "No",
      simulado: simulado.uso_horario_pico ? "Sí" : "No",
      diferencia: actual.uso_horario_pico === simulado.uso_horario_pico ? "Sin cambios" : "Cambió",
    },
  ];

  return (
    <Card className="min-w-0">
      <h3 className="text-sm font-medium text-slate-200">Qué estás comparando</h3>
      <p className="mt-1 text-xs text-slate-500">
        El escenario actual es la referencia fija; los controles modifican únicamente el simulado.
      </p>

      <div className="mt-4 overflow-x-auto">
        <div className="min-w-[430px]">
          <div className="grid grid-cols-[1.4fr_1fr_1fr_1fr] gap-2 border-b border-slate-800 pb-2 text-xs font-medium text-slate-500">
            <span>Variable</span>
            <span>Actual</span>
            <span>Simulado</span>
            <span>Diferencia</span>
          </div>
          {filas.map((fila) => (
            <div
              key={fila.etiqueta}
              className="grid grid-cols-[1.4fr_1fr_1fr_1fr] gap-2 border-b border-slate-800/70 py-2.5 text-sm last:border-0"
            >
              <span className="text-slate-400">{fila.etiqueta}</span>
              <span className="tabular-nums text-slate-300">{fila.actual}</span>
              <span className="tabular-nums text-slate-100">{fila.simulado}</span>
              <span className="tabular-nums text-slate-400">{fila.diferencia}</span>
            </div>
          ))}
        </div>
      </div>

      {base && resultado && (
        <div className="mt-4 flex flex-wrap items-center justify-between gap-3 rounded-xl bg-slate-950/60 p-3">
          <div>
            <p className="text-xs text-slate-500">Categoría actual</p>
            <Badge categoria={base.categoria} className="mt-1" />
          </div>
          <span className="text-slate-600" aria-hidden="true">→</span>
          <div className="text-right">
            <p className="text-xs text-slate-500">Categoría simulada</p>
            <Badge categoria={resultado.categoria} className="mt-1" />
          </div>
        </div>
      )}
    </Card>
  );
}
