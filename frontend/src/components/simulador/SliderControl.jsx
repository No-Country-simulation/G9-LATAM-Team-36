// Slider reutilizable del simulador (etiqueta + valor + rango).
// Local a esta vista (regla anti-bloqueo de la guía).
export default function SliderControl({ label, value, min, max, step = 1, unit = "", onChange }) {
  return (
    <div>
      <div className="mb-1.5 flex items-baseline justify-between">
        <label className="text-sm font-medium text-slate-300">{label}</label>
        <span className="text-sm font-semibold tabular-nums text-brand">
          {value}
          {unit}
        </span>
      </div>
      <input
        type="range"
        min={min}
        max={max}
        step={step}
        value={value}
        onChange={(e) => onChange(Number(e.target.value))}
        className="w-full cursor-pointer accent-brand"
        aria-label={label}
      />
    </div>
  );
}
