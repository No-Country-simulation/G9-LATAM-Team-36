import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Layout from "./components/layout/Layout";
import Home from "./pages/Home";
import Analisis from "./pages/Analisis";
import Historial from "./pages/Historial";
import Simulador from "./pages/Simulador";

// Router de la app. Cada ruta la construye una persona distinta (ver GUIA_FRONTEND.md).
// NO agregar lógica de vistas aquí — solo el enrutado.
export default function App() {
  return (
    <BrowserRouter>
      <Layout>
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/analizar" element={<Analisis />} />
          <Route path="/historial" element={<Historial />} />
          <Route path="/simulador" element={<Simulador />} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </Layout>
    </BrowserRouter>
  );
}
