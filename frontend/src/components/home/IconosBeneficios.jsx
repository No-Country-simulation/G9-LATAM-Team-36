function EstilosAnimacion() {
  return (
    <style>{`
      @keyframes oscilar-aguja {
        0%, 100% { transform: rotate(-65deg); }
        50% { transform: rotate(65deg); }
      }
      @keyframes pulsar-brillo {
        0%, 100% { opacity: 0.5; filter: drop-shadow(0 0 2px #eab308); }
        50% { opacity: 1; filter: drop-shadow(0 0 8px #eab308); }
      }
      @keyframes girar-moneda {
        0% { transform: rotateY(0deg); }
        100% { transform: rotateY(360deg); }
      }
      .anim-aguja {
        animation: oscilar-aguja 3.5s ease-in-out infinite;
        transform-origin: 50px 55px;
      }
      .anim-brillo {
        animation: pulsar-brillo 2.5s ease-in-out infinite;
      }
      .anim-moneda {
        animation: girar-moneda 4s linear infinite;
        transform-style: preserve-3d;
      }
    `}</style>
  );
}

export function IconoVelocimetro() {
  return (
    <>
      <EstilosAnimacion />
      <svg viewBox="0 0 100 65" className="h-12 w-20">
        <path d="M12,55 A38,38 0 0,1 31,22.1" fill="none" stroke="#ef4444" strokeOpacity="0.35" strokeWidth="8" strokeLinecap="round" />
        <path d="M31,22.1 A38,38 0 0,1 69,22.1" fill="none" stroke="#eab308" strokeOpacity="0.35" strokeWidth="8" strokeLinecap="round" />
        <path d="M69,22.1 A38,38 0 0,1 88,55" fill="none" stroke="#22c55e" strokeOpacity="0.35" strokeWidth="8" strokeLinecap="round" />
        <line x1="50" y1="55" x2="50" y2="20" stroke="#10b981" strokeWidth="3" strokeLinecap="round" className="anim-aguja" />
        <circle cx="50" cy="55" r="4" fill="#10b981" />
      </svg>
    </>
  );
}

export function IconoBombillo() {
  return (
    <>
      <EstilosAnimacion />
      <svg viewBox="0 0 24 24" className="h-12 w-12 anim-brillo" fill="none" stroke="#eab308" strokeWidth="1.5">
        <path d="M9 18h6M10 21h4M12 3a6 6 0 0 0-3.5 10.9c.5.4.8 1 .8 1.6v.5h5.4v-.5c0-.6.3-1.2.8-1.6A6 6 0 0 0 12 3Z" strokeLinecap="round" strokeLinejoin="round" />
      </svg>
    </>
  );
}

export function IconoMoneda() {
  return (
    <>
      <EstilosAnimacion />
      <div style={{ perspective: "200px" }} className="h-12 w-12">
        <svg viewBox="0 0 24 24" className="h-12 w-12 anim-moneda" fill="none" stroke="#10b981" strokeWidth="1.5">
          <circle cx="12" cy="12" r="9" strokeLinecap="round" />
          <path d="M12 7v10M9.5 9.2c0-1 1-1.7 2.5-1.7s2.5.7 2.5 1.6c0 2.2-5 1-5 3.2 0 1 1 1.7 2.5 1.7s2.5-.7 2.5-1.7" strokeLinecap="round" strokeLinejoin="round" />
        </svg>
      </div>
    </>
  );
}