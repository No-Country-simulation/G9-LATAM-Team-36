#!/bin/bash
# Bloque J — Deploy en un comando: correr en la VM dentro de la carpeta del repo.

set -e

echo "Actualizando código..."
git pull

echo "Reconstruyendo y levantando servicios..."
docker compose up -d --build

echo "Esperando healthchecks..."
sleep 10
docker compose ps
