import Card from "../ui/Card";

export default function ListaRecomendaciones({ recomendaciones }) {
    if (!recomendaciones?.length) return null;

    return (
        <div>
            <p className="label mb-2">Recomendaciones</p>
            <div className="space-y-2">
                {recomendaciones.map((texto, i) => (
                    <Card key={i} className="text-sm text-slate-300">
                        {texto}
                    </Card>
                ))}
            </div>
        </div>
    );
}