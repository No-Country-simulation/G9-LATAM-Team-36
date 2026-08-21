import { useEffect, useRef, useState } from "react";
import Hero from "../components/home/Hero";
import Beneficios from "../components/home/Beneficios";
import FranjaSemaforo from "../components/home/FranjaSemaforo";

// Una entrada por sección — agregar/quitar aquí si cambian las secciones
const SECCIONES = [
  { id: "hero", label: "Inicio" },
  { id: "beneficios", label: "Beneficios" },
  { id: "extra", label: "Extra" },
];

// Alto del navbar (sticky, py-3) + padding del <main> del Layout compartido.
const OFFSET_NAVBAR = "89px";

export default function Home() {
  const containerRef = useRef(null);
  const sectionRefs = useRef([]);
  const [activa, setActiva] = useState(0);

  useEffect(() => {
    const contenedor = containerRef.current;
    if (!contenedor) return;

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            const index = sectionRefs.current.indexOf(entry.target);
            if (index !== -1) setActiva(index);
          }
        });
      },
      { root: contenedor, threshold: 0.6 }
    );

    sectionRefs.current.forEach((sec) => sec && observer.observe(sec));
    return () => observer.disconnect();
  }, []);

  const irASeccion = (index) => {
    sectionRefs.current[index]?.scrollIntoView({
      behavior: "smooth",
      block: "start",
    });
  };

  return (
    <div className="relative">
      {/* Oculta la barra de scroll nativa pero deja el scroll funcionando */}
      <style>{`
        .snap-container::-webkit-scrollbar {
          display: none;
        }
      `}</style>

      <div
        ref={containerRef}
        className="snap-container -mx-4 -my-8 snap-y snap-mandatory overflow-y-scroll scroll-smooth"
        style={{
          height: `calc(100dvh - ${OFFSET_NAVBAR})`,
          scrollbarWidth: "none",
          msOverflowStyle: "none",
        }}
      >
        {/* Sección 1 — Hero */}
        <section
          ref={(el) => (sectionRefs.current[0] = el)}
          className="snap-start overflow-hidden"
          style={{ height: `calc(100dvh - ${OFFSET_NAVBAR})` }}
        >
          <Hero />
        </section>

        {/* Sección 2 — Beneficios */}
        <section
          ref={(el) => (sectionRefs.current[1] = el)}
          className="snap-start overflow-hidden"
          style={{ height: `calc(100dvh - ${OFFSET_NAVBAR})` }}
        >
          <Beneficios />
        </section>

        {/* Sección 3 — Franja de semáforo */}
        <section
          ref={(el) => (sectionRefs.current[2] = el)}
          className="snap-start overflow-hidden"
          style={{ height: `calc(100dvh - ${OFFSET_NAVBAR})` }}
        >
          <FranjaSemaforo />
        </section>
      </div>

      {/* Punticos de navegación */}
      <div className="fixed right-6 top-1/2 z-20 flex -translate-y-1/2 flex-col gap-3">
        {SECCIONES.map((s, i) => (
          <button
            key={s.id}
            onClick={() => irASeccion(i)}
            aria-label={`Ir a ${s.label}`}
            className={`h-2.5 w-2.5 rounded-full transition ${
              activa === i
                ? "bg-brand"
                : "bg-slate-700 hover:bg-slate-500"
            }`}
          />
        ))}
      </div>
    </div>
  );
}