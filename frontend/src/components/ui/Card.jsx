// Contenedor tipo panel. Úsalo para agrupar contenido en todas las vistas.
export default function Card({ className = "", children }) {
  return <div className={`card ${className}`}>{children}</div>;
}
