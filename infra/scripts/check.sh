#!/usr/bin/env bash
# Bloque J — Verificación de salud de los servicios (smoke test).
#
# Uso:  bash infra/scripts/check.sh [host]
#   host: por defecto "localhost" (en la VM de OCI también es localhost,
#         los servicios corren ahí mismo con docker compose).
#
# Devuelve exit code != 0 si algún servicio falla -> útil para deploy.sh y el Demo Day.

set -u
HOST="${1:-localhost}"
FALLOS=0

ok()   { echo "✅ $1"; }
fail() { echo "❌ $1"; FALLOS=$((FALLOS + 1)); }

echo "Verificando servicios en $HOST ..."
echo ""

# ml-service — health check propio
if curl -sf "http://$HOST:8000/health" >/dev/null; then
  ok "ml-service  (GET :8000/health)"
else
  fail "ml-service  (GET :8000/health)"
fi

# backend — prueba FUNCIONAL: el ejemplo del brief debe responder 2xx
EJEMPLO='{"consumo_kwh":420,"uso_horario_pico":true,"cantidad_equipos":10,"tipo_inmueble":"Casa","horas_alto_consumo":8}'
if curl -sf -X POST "http://$HOST:8080/api/analisis-energetico" \
        -H "Content-Type: application/json" -d "$EJEMPLO" >/dev/null; then
  ok "backend     (POST :8080/api/analisis-energetico)"
else
  fail "backend     (POST :8080/api/analisis-energetico)"
fi

# frontend — sirve la página
if curl -sf -o /dev/null "http://$HOST:80"; then
  ok "frontend    (GET :80)"
else
  fail "frontend    (GET :80)"
fi

echo ""
if [ "$FALLOS" -eq 0 ]; then
  echo "🟢 Todo OK en $HOST"
else
  echo "🔴 $FALLOS servicio(s) con problemas"
fi
exit "$FALLOS"
