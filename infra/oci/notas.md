# Notas de OCI — Bloque J

Ver la página de Notion "Arquitectura OCI — EnergiAI" para la justificación
completa. Aquí solo la bitácora operativa.

## Object Storage
- [ ] Bucket `energiai-models` (privado) creado
- [ ] Namespace: _______
- [ ] Región: _______
- [ ] API Key generada y compartida por canal privado (NUNCA en el repo)

## Compute (VM)
- [ ] Shape: VM.Standard.A1.Flex (Always Free ARM)
- [ ] OCPUs / RAM elegidos: _______
- [ ] Availability Domain que sí tuvo capacidad: _______
- [ ] IP pública: _______
- [ ] Usuario SSH: `ubuntu` (o el que corresponda a la imagen)

## Security List / NSG
- [ ] Puerto 22 (SSH) — solo IPs del equipo
- [ ] Puerto 80 (HTTP) — público
- [ ] Puerto 443 (HTTPS) — público (si se configura TLS)
- [ ] Puertos 8080/8000/5432 — NO expuestos, solo red interna de Docker

## Intentos de aprovisionamiento (si "Out of capacity")
| Fecha | Availability Domain | Resultado |
|---|---|---|
| | | |
