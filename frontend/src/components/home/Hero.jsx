import { Link } from "react-router-dom";
import RadarFondo from "./RadarFondo";

export default function Hero() {
  return (
    <div className="relative flex h-full items-center justify-center overflow-hidden">
      <RadarFondo />

      <div className="relative z-10 mx-auto max-w-4xl px-4 text-center">
        <h1 className="text-3xl font-extrabold leading-[1.1] tracking-tight text-white sm:text-5xl lg:text-6xl">
          Convierte tus{" "}
          <span className="text-brand">datos eléctricos</span>{" "}
          en decisiones inteligentes.
        </h1>

        <p className="mx-auto mt-4 max-w-lg text-base leading-7 text-slate-400 sm:mt-6 sm:text-lg sm:leading-8">
          Analiza tu consumo eléctrico y recibe recomendaciones para ahorrar
          energía, optimizar tus hábitos y reducir el costo de tu factura.
        </p>

        <div className="mt-6 sm:mt-10">
          <Link to="/analizar" className="btn-primary inline-block">
            Analizar mi consumo
          </Link>
        </div>
      </div>
    </div>
  );
}