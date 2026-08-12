const POSICIONES_BLIPS = [
  { top: "20%", left: "35%", delay: "0s" },
  { top: "60%", left: "70%", delay: "1s" },
  { top: "75%", left: "25%", delay: "2s" },
  { top: "30%", left: "68%", delay: "3s" },
  { top: "50%", left: "50%", delay: "4s" },
  { top: "15%", left: "55%", delay: "5s" },
  { top: "80%", left: "55%", delay: "6s" },
  { top: "45%", left: "20%", delay: "7s" },
  { top: "40%", left: "80%", delay: "8s" },
];

export default function RadarFondo() {
  return (
    <div className="pointer-events-none absolute inset-0 flex items-center justify-center overflow-hidden">
      <style>{`
        @keyframes radar-pulso {
          0% { transform: scale(0.05); opacity: 0; }
          10% { opacity: 0.8; }
          70% { opacity: 0.5; }
          100% { transform: scale(1); opacity: 0; }
        }
        .anillo-radar {
          animation: radar-pulso 4s ease-out infinite;
          animation-fill-mode: backwards;
        }
        @keyframes blip-destello {
          0%, 100% { transform: scale(0); opacity: 0; }
          20% { transform: scale(1); opacity: 0.5; }
          50% { transform: scale(1.4); opacity: 0; }
        }
        .blip {
          animation: blip-destello 9s ease-out infinite;
          animation-fill-mode: backwards;
        }
      `}</style>

      <div className="relative h-[300px] w-[300px] sm:h-[450px] sm:w-[450px] lg:h-[600px] lg:w-[600px]">
        <span className="anillo-radar absolute inset-0 rounded-full border border-brand" style={{ animationDelay: "0s" }} />
        <span className="anillo-radar absolute inset-0 rounded-full border border-brand" style={{ animationDelay: "1.3s" }} />
        <span className="anillo-radar absolute inset-0 rounded-full border border-brand" style={{ animationDelay: "2.6s" }} />

        {POSICIONES_BLIPS.map((punto, i) => (
          <span
            key={i}
            className="blip absolute h-1.5 w-1.5 rounded-full bg-brand"
            style={{
              top: punto.top,
              left: punto.left,
              animationDelay: punto.delay,
            }}
          />
        ))}
      </div>
    </div>
  );
}