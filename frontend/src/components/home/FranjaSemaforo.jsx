import { useEffect, useRef, useState } from "react";
import { estiloDe } from "../../lib/categoria";

const CATEGORIAS = [
  {
    nombre: "Ineficiente",
    descripcion: "Alto consumo, mayor costo mensual. Te damos recomendaciones para bajarlo.",
  },
  {
    nombre: "Moderado",
    descripcion: "Consumo dentro del rango esperado, con espacio para pequeños ajustes.",
  },
  {
    nombre: "Eficiente",
    descripcion: "Bajo consumo, ahorro constante. Vale la pena mantener tus hábitos.",
  },
];

const DURACION_RECORRIDO = 9500;

function categoriaSegunPosicion(porcentaje) {
  if (porcentaje < 33.33) return "Ineficiente";
  if (porcentaje < 66.66) return "Moderado";
  return "Eficiente";
}

export default function FranjaSemaforo() {
  const [posicion, setPosicion] = useState(0);
  const animacionRef = useRef(null);

  useEffect(() => {
    const inicio = performance.now();

    const animar = (ahora) => {
      const transcurrido = (ahora - inicio) % (DURACION_RECORRIDO * 2);
      const progreso = Math.min(
        transcurrido / DURACION_RECORRIDO,
        2 - transcurrido / DURACION_RECORRIDO
      );
      setPosicion(Math.max(0, Math.min(100, progreso * 100)));
      animacionRef.current = requestAnimationFrame(animar);
    };

    animacionRef.current = requestAnimationFrame(animar);
    return () => cancelAnimationFrame(animacionRef.current);
  }, []);

  const categoriaActiva = categoriaSegunPosicion(posicion);
  const estiloActivo = estiloDe(categoriaActiva);

  return (
    <div className="mx-auto flex h-full max-w-3xl flex-col justify-center px-4">
      <h2 className="text-center text-xl font-bold text-white sm:text-3xl md:text-4xl">
        Así clasificamos tu consumo
      </h2>
      <p className="mt-2 text-center text-sm text-slate-400 sm:mt-3 sm:text-base">
        Cada análisis ubica tu consumo en un semáforo de eficiencia.
      </p>

      {/* Barra espectro */}
      <div className="relative mt-8 h-3 w-full rounded-full bg-gradient-to-r from-ineficiente via-moderado to-eficiente sm:mt-16">
        <div
          className="absolute top-1/2 h-7 w-7 -translate-x-1/2 -translate-y-1/2 rounded-full border-4 border-slate-950 shadow-lg transition-colors duration-300 sm:h-8 sm:w-8"
          style={{ left: `${posicion}%`, backgroundColor: estiloActivo.hex }}
        />
      </div>

      {/* Móvil: lista vertical con línea de color. Desde sm: 3 columnas horizontal */}
      <div className="mt-6 flex flex-col gap-3 sm:mt-10 sm:grid sm:grid-cols-3 sm:gap-4 sm:text-center">
        {CATEGORIAS.map(({ nombre, descripcion }) => {
          const estilo = estiloDe(nombre);
          const activa = nombre === categoriaActiva;
          return (
            <div key={nombre} className="flex items-stretch gap-3 sm:block">
              {/* Línea vertical de color, solo visible en móvil */}
              <span
                className="w-1 shrink-0 rounded-full transition-colors duration-300 sm:hidden"
                style={{
                  backgroundColor: activa ? estilo.hex : "#334155",
                }}
              />
              <div>
                <span
                  className={`text-base font-semibold transition-all duration-300 sm:text-lg ${
                    activa ? "opacity-100" : "text-slate-500 opacity-60 sm:opacity-40"
                  }`}
                  style={activa ? { color: estilo.hex } : undefined}
                >
                  {estilo.label}
                </span>
                <p
                  className={`mt-1 text-sm transition-opacity duration-300 sm:mt-1.5 ${
                    activa ? "text-slate-300 opacity-100" : "text-slate-500 opacity-60 sm:text-slate-600 sm:opacity-40"
                  }`}
                >
                  {descripcion}
                </p>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}