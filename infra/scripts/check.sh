#!/bin/bash
# Bloque J — Verificación rápida antes del Demo Day.

echo "Backend health:"
curl -sf http://localhost:8080/actuator/health || echo "❌ backend no responde"

echo ""
echo "ML service health:"
curl -sf http://localhost:8000/health || echo "❌ ml-service no responde"

echo ""
echo "Frontend:"
curl -sf -o /dev/null -w "%{http_code}\n" http://localhost:80 || echo "❌ frontend no responde"
