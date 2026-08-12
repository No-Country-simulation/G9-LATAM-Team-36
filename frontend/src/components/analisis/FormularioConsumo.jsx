import { useState } from "react";
import Button from "../ui/Button";

const VALORES_INICIALES = {
    consumo_kwh: "",
    uso_horario_pico: false,
    cantidad_equipos: "",
    tipo_inmueble: "Casa",
    horas_alto_consumo: "",
};

export default function FormularioConsumo({ onAnalizar, loading }) {
    const [datos, setDatos] = useState(VALORES_INICIALES);

    function actualizarCampo(campo, valor) {
        setDatos((prev) => ({ ...prev, [campo]: valor }));
    }

    function manejarSubmit(e) {
        e.preventDefault();
        onAnalizar({
            ...datos,
            consumo_kwh: Number(datos.consumo_kwh),
            cantidad_equipos: Number(datos.cantidad_equipos),
            horas_alto_consumo: Number(datos.horas_alto_consumo),
        });
    }

    return (
        <form onSubmit={manejarSubmit} className="space-y-4">
            <div>
                <label className="label">Consumo mensual (kWh)</label>
                <input
                    type="number"
                    className="field"
                    required
                    min="0"
                    step="0.1"
                    value={datos.consumo_kwh}
                    onChange={(e) => actualizarCampo("consumo_kwh", e.target.value)}
                />
            </div>

            <div>
                <label className="label">Tipo de inmueble</label>
                <select
                    className="field"
                    value={datos.tipo_inmueble}
                    onChange={(e) => actualizarCampo("tipo_inmueble", e.target.value)}
                >
                    <option value="Casa">Casa</option>
                    <option value="Departamento">Departamento</option>
                    <option value="Local">Local</option>
                </select>
            </div>

            <div>
                <label className="label">Cantidad de equipos</label>
                <input
                    type="number"
                    className="field"
                    required
                    min="1"
                    value={datos.cantidad_equipos}
                    onChange={(e) => actualizarCampo("cantidad_equipos", e.target.value)}
                />
            </div>

            <div>
                <label className="label">Horas de alto consumo al dia (0-24)</label>
                <input
                    type="number"
                    className="field"
                    required
                    min="0"
                    max="24"
                    value={datos.horas_alto_consumo}
                    onChange={(e) => actualizarCampo("horas_alto_consumo", e.target.value)}
                />
            </div>

            <div className="flex items-center gap-2">
                <label className="flex items-center gap-3 cursor-pointer">
                    <span className="label">Uso en horario pico</span>
                    <button
                        type="button"
                        role="switch"
                        aria-checked={datos.uso_horario_pico}
                        onClick={() => actualizarCampo("uso_horario_pico", !datos.uso_horario_pico)}
                        className={`relative h-6 w-11 rounded-full transition-colors ${datos.uso_horario_pico ? "bg-brand" : "bg-slate-700"
                            }`}
                    >
                        <span
                            className={`absolute top-0.5 left-0.5 h-5 w-5 rounded-full bg-white transition-transform ${datos.uso_horario_pico ? "translate-x-5" : ""
                                }`}
                        />
                    </button>
                </label>
            </div>

            <Button type="submit" disabled={loading}>
                {loading ? "Analizando..." : "Analizar"}
            </Button>
        </form>
    );
}