import { NavLink } from "react-router-dom";

const LINKS = [
  { to: "/", label: "Inicio", end: true },
  { to: "/analizar", label: "Análisis" },
  { to: "/historial", label: "Historial" },
  { to: "/simulador", label: "Simulador" },
];

export default function Navbar() {
  return (
    <header className="sticky top-0 z-10 border-b border-slate-800 bg-slate-950/80 backdrop-blur">
      <nav className="mx-auto flex max-w-5xl items-center justify-between px-4 py-3">
        <span className="text-lg font-bold">
          Energi<span className="text-brand">AI</span>
        </span>
        <div className="flex gap-1">
          {LINKS.map((l) => (
            <NavLink
              key={l.to}
              to={l.to}
              end={l.end}
              className={({ isActive }) =>
                `rounded-lg px-3 py-1.5 text-sm font-medium transition ${
                  isActive
                    ? "bg-brand/15 text-brand"
                    : "text-slate-400 hover:text-slate-100"
                }`
              }
            >
              {l.label}
            </NavLink>
          ))}
        </div>
      </nav>
    </header>
  );
}
