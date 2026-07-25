#!/usr/bin/env bash
# Bloque J — Deploy en un comando. Correr en la VM de OCI:
#   bash infra/scripts/deploy.sh
# Trae los últimos cambios, reconstruye y levanta todo, y verifica la salud.

set -e

# Ubicarse en la raíz del repo (este script vive en infra/scripts/)
cd "$(dirname "$0")/../.."

echo "▸ Trayendo últimos cambios de git..."
git pull

echo "▸ Reconstruyendo y levantando los servicios..."
docker compose up -d --build

echo "▸ Esperando a que los servicios queden sanos..."
sleep 15

echo "▸ Estado de los contenedores:"
docker compose ps

echo ""
echo "▸ Verificación de salud:"
bash "$(dirname "$0")/check.sh"
