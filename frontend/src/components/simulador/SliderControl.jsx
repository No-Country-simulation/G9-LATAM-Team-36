import { useEffect, useState } from "react";

// Slider reutilizable del comparador: permite arrastrar o escribir un valor exacto.
export default function SliderControl({ label, value, min, max, step = 1, unit = "", onChange }) {
  const [draft, setDraft] = useState(String(value));

  useEffect(() => {
    setDraft(String(value));
  }, [value]);

  function confirmarValor() {
    const numero = Number(draft);
    if (!Number.isFinite(numero)) {
      setDraft(String(value));
      return;
    }

    const ajustado = Math.min(max, Math.max(min, numero));
    setDraft(String(ajustado));
    onChange(ajustado);
  }

  return (
    <div>
      <div className="mb-2 flex items-center justify-between gap-4">
        <label className="text-sm font-medium text-slate-300">{label}</label>
        <div className="flex items-center gap-1.5">
          <input
            type="number"
            min={min}
            max={max}
            step={step}
            value={draft}
            onChange={(event) => setDraft(event.target.value)}
            onBlur={confirmarValor}
            onKeyDown={(event) => {
              if (event.key === "Enter") confirmarValor();
            }}
            className="field w-24 px-2 py-1 text-right text-sm tabular-nums"
            aria-label={`${label}, valor exacto`}
          />
          {unit && <span className="text-xs text-slate-400">{unit.trim()}</span>}
        </div>
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
      <div className="mt-1 flex justify-between text-xs tabular-nums text-slate-500">
        <span>{min}{unit}</span>
        <span>{max}{unit}</span>
      </div>
    </div>
  );
}
