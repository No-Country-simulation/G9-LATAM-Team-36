/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  darkMode: "class",
  theme: {
    extend: {
      colors: {
        // Marca (verde energía/eficiencia)
        brand: {
          DEFAULT: "#10b981", // emerald-500
          hover: "#059669", // emerald-600
        },
        // Semáforo de eficiencia — usar SIEMPRE estos para las categorías
        eficiente: "#22c55e",
        moderado: "#eab308",
        ineficiente: "#ef4444",
      },
      fontFamily: {
        sans: ["Inter", "system-ui", "sans-serif"],
      },
    },
  },
  plugins: [],
};
