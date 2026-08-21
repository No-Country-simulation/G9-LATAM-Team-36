import Badge from "../ui/Badge";
import { formatoMoneda } from "../../lib/categoria";

export default function ResultadoCard({ resultado }) {
    const { categoria, probabilidad, costo_estimado_mensual } = resultado;

    return (
        <div className="space-y-4">
            <div className="flex items-center justify-between">
                <Badge categoria={categoria} />
                <span className="text-sm text-slate-400">
                    {Math.round(probabilidad * 100)}% de confianza
                </span>
            </div>

            <div>
                <p className="label">Costo estimado mensual</p>
                <p className="text-3xl font-bold text-slate-100">
                    {formatoMoneda(costo_estimado_mensual)}
                </p>
            </div>
        </div>
    );
}