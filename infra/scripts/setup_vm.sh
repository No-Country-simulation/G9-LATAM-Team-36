#!/bin/bash
# Bloque J — Instala Docker y Docker Compose en una VM Ubuntu limpia de OCI.
# Ejecutar UNA VEZ en la VM: bash setup_vm.sh

set -e

echo "Actualizando paquetes..."
sudo apt-get update -y

echo "Instalando Docker..."
curl -fsSL https://get.docker.com | sudo sh
sudo usermod -aG docker "$USER"

echo "Instalando plugin docker compose..."
sudo apt-get install -y docker-compose-plugin

echo "Listo. Cierra sesión y vuelve a entrar para usar docker sin sudo."
