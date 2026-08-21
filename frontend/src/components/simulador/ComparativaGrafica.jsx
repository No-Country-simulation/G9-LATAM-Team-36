import {
  BarChart,
  Bar,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import { formatoMoneda } from "../../lib/categoria";

// Barra comparativa: costo mensual actual vs simulado.
// Actual en gris neutro, simulado en verde de marca.
export default function ComparativaGrafica({ actual, simulado }) {
  const data = [
    { nombre: "Actual", costo: actual },
    { nombre: "Simulado", costo: simulado },
  ];

  return (
    <ResponsiveContainer width="100%" height={220}>
      <BarChart data={data} margin={{ top: 8, right: 8, bottom: 0, left: 0 }}>
        <CartesianGrid strokeDasharray="3 3" stroke="#1e293b" vertical={false} />
        <XAxis dataKey="nombre" stroke="#94a3b8" tickLine={false} axisLine={false} />
        <YAxis
          stroke="#94a3b8"
          width={52}
          tickLine={false}
          axisLine={false}
          tickFormatter={(v) => `$${v}`}
        />
        <Tooltip
          cursor={{ fill: "rgba(30,41,59,0.4)" }}
          contentStyle={{
            background: "#0f172a",
            border: "1px solid #334155",
            borderRadius: 12,
            color: "#f1f5f9",
          }}
          formatter={(v) => [formatoMoneda(v), "Costo mensual"]}
        />
        <Bar dataKey="costo" radius={[8, 8, 0, 0]} maxBarSize={90} isAnimationActive={false}>
          <Cell fill="#64748b" />
          <Cell fill={simulado <= actual ? "#10b981" : "#ef4444"} />
        </Bar>
      </BarChart>
    </ResponsiveContainer>
  );
}
