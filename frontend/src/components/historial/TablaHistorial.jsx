import Badge from "../ui/Badge";
import { formatoMoneda } from "../../lib/categoria";

export default function TablaHistorial({ data }) {
    if (!data || data.length === 0) {
        return <p className="text-slate-400">No hay análisis guardados.</p>;
    }

    return (
        <div className="overflow-x-auto">
            <table className="w-full text-sm">
                <thead className="border-b border-slate-800 text-slate-400">
                <tr>
                    <th className="text-left py-2">Fecha</th>
                    <th className="text-left py-2">Consumo</th>
                    <th className="text-left py-2">Inmueble</th>
                    <th className="text-left py-2">Categoría</th>
                    <th className="text-right py-2">Costo</th>
                </tr>
                </thead>
                <tbody>
                {data.map((item) => (
                    <tr key={item.id} className="border-b border-slate-800/50 hover:bg-slate-800/30">
                        <td className="py-2">
                            {item.creado_en ? new Date(item.creado_en).toLocaleString(undefined, {
                                year: 'numeric',
                                month: '2-digit',
                                day: '2-digit',
                                hour: '2-digit',
                                minute: '2-digit',
                                second: '2-digit',
                                hour12: false,
                            }) : '—'}
                        </td>
                        <td className="py-2">{item.consumo_kwh} kWh</td>
                        <td className="py-2">{item.tipo_inmueble}</td>
                        <td className="py-2"><Badge categoria={item.categoria} /></td>
                        <td className="py-2 text-right">{formatoMoneda(item.costo_estimado_mensual)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}