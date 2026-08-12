import Card from "../ui/Card";
import { IconoVelocimetro, IconoBombillo, IconoMoneda } from "./IconosBeneficios";

const BENEFICIOS = [
  {
    titulo: "Clasifica tu eficiencia",
    descripcion:
      "Tu consumo se evalúa y se ubica en un semáforo claro: eficiente, moderado o ineficiente.",
    Icono: IconoVelocimetro,
  },
  {
    titulo: "Recibe recomendaciones",
    descripcion:
      "Recibe pasos claros para reducir tu consumo, según tu tipo de inmueble y hábitos.",
    Icono: IconoBombillo,
  },
  {
    titulo: "Estima tu costo",
    descripcion: "Descubre cuánto te cuesta tu consumo actual al mes.",
    Icono: IconoMoneda,
  },
];

export default function Beneficios() {
  return (
    <div className="mx-auto flex h-full max-w-5xl flex-col justify-center px-4">
      <h2 className="text-center text-xl font-bold text-white sm:text-3xl md:text-4xl">
        ¿Qué puedes descubrir con Energi<span className="text-brand">AI</span>?
      </h2>

      <div className="mt-4 grid gap-3 sm:mt-12 sm:gap-6 sm:grid-cols-3">
        {BENEFICIOS.map(({ titulo, descripcion, Icono }) => (
          <Card
            key={titulo}
            className="flex flex-col items-center p-3 text-center sm:p-6"
          >
            <div className="scale-75 sm:scale-100">
              <Icono />
            </div>
            <h3 className="mt-1 text-sm font-semibold text-slate-100 sm:mt-4 sm:text-lg">
              {titulo}
            </h3>
            <p className="mt-1 text-xs text-slate-400 sm:mt-2 sm:text-sm">
              {descripcion}
            </p>
          </Card>
        ))}
      </div>
    </div>
  );
}