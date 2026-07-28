import Navbar from "./Navbar";

// Envuelve todas las páginas: navbar arriba + contenedor centrado.
export default function Layout({ children }) {
  return (
    <div className="min-h-screen bg-slate-950 text-slate-100">
      <Navbar />
      <main className="mx-auto max-w-5xl px-4 py-8">{children}</main>
    </div>
  );
}
