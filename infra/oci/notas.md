# Aprovisionamiento de OCI — Bloque J

Guía paso a paso para poner EnergiAI en producción sobre Oracle Cloud Infrastructure.
Dos piezas obligatorias del brief: **Object Storage** (guarda el modelo) y **Compute**
(aloja los 4 servicios con Docker Compose en una URL pública).

> ⚠️ **Nunca subir llaves al repo.** Los archivos `.pem`, `.oci/` y `config` ya están
> en el `.gitignore`. Las credenciales se comparten por canal privado del equipo.

---

## Parte 1 — Object Storage (el modelo del Bloque C)

**Objetivo:** un bucket privado donde el Bloque C sube `modelo.joblib` y el Bloque D
lo descarga.

1. Consola OCI → **Storage → Buckets** → *Create Bucket*.
   - Name: `energiai-models` · Visibility: **Private**.
2. Anotar el **Namespace** (aparece en los detalles del bucket) y la **Región**.
3. Crear una **API Key** para autenticarse desde los scripts de Python:
   - Perfil (arriba a la derecha) → *My profile* → **API keys** → *Add API key*.
   - Descargar la **llave privada** (`.pem`) — NO se sube al repo.
   - Copiar el bloque de *Configuration file preview* que muestra OCI.
4. En la máquina que suba/descargue el modelo, crear `~/.oci/config` con ese bloque
   y apuntar `key_file` a la ruta del `.pem` descargado.
5. Probar la subida del modelo (desde la raíz del repo, con el venv de data-science):
   ```bash
   python data-science/src/upload_model.py            # sube modelo.joblib + metadata.json
   ```
6. Para que el **ml-service en la VM** descargue el modelo de OCI en vez de usar el
   volumen local, se levanta con `USE_LOCAL_MODEL=false` y las variables `OCI_*` del
   `.env`. (En local seguimos con `USE_LOCAL_MODEL=true`, que monta el modelo por volumen.)

### Bitácora Object Storage
- [x] Bucket `energiai-models` (privado) creado — modelo y metadata subidos ✅
- [x] Namespace: `axdjeqy6h1zi`
- [x] Región: `mx-queretaro-1` (Mexico Central — Querétaro)
- [x] API Key generada · llave privada en la máquina de Omar (fuera del repo)

> **Credenciales (NO en el repo):** el SDK lee `~/.oci/config`; la llave privada
> está en `~/Documentos/OCI WEPLAY32/energiai_api_key_priv.pem`. Los OCID y el
> fingerprint viven dentro de ese `config` — nunca se commitean.
>
> **Subir/actualizar el modelo:** `python data-science/src/upload_model.py`
> (usa el perfil DEFAULT de `~/.oci/config`).

---

## Parte 2 — Compute (la VM que aloja todo)

**Objetivo:** una VM Always Free ARM con Docker, corriendo los 4 servicios.

> 💡 **Empezar cuanto antes.** La VM gratuita ARM (A1.Flex) a veces da *"Out of
> capacity"*; hay que reintentar en distintos Availability Domains y a distintas horas.

1. Consola OCI → **Compute → Instances** → *Create Instance*.
   - **Image:** Ubuntu 22.04 o 24.04.
   - **Shape:** `VM.Standard.A1.Flex` (Always Free ARM). Pedir **2 OCPU / 12 GB** es más
     fácil de conseguir que el máximo de 4/24.
   - **SSH keys:** subir tu llave pública (`~/.ssh/id_ed25519.pub` o similar).
2. Si sale *"Out of capacity"*: cambiar de **Availability Domain** y reintentar (anotar
   abajo cada intento).
3. Al crearse, anotar la **IP pública** y el **usuario SSH** (en Ubuntu suele ser `ubuntu`).
4. Conectarse: `ssh ubuntu@<IP_PUBLICA>`.

### Bitácora Compute
- [ ] Shape: `VM.Standard.A1.Flex` · OCPUs/RAM: `_______`
- [ ] Availability Domain con capacidad: `_______`
- [ ] IP pública: `_______`
- [ ] Usuario SSH: `_______`

### Intentos de aprovisionamiento (si "Out of capacity")
| Fecha | Availability Domain | Resultado |
|---|---|---|
| | | |

---

## Parte 3 — Red (Security List / NSG)

**Objetivo:** exponer solo lo necesario. Los puertos internos (backend, ml-service,
postgres) NO se abren a internet: viven en la red interna de Docker.

En la **VCN → Subnet → Security List** (o una NSG), agregar reglas de *Ingress*:

| Puerto | Origen | Uso |
|---|---|---|
| 22 (SSH) | solo IPs del equipo | administración |
| 80 (HTTP) | `0.0.0.0/0` | frontend público |
| 443 (HTTPS) | `0.0.0.0/0` | si se configura TLS |
| 8080 / 8000 / 5432 | **NO exponer** | solo red interna de Docker |

> En Ubuntu, OCI trae `iptables` restrictivo por defecto. Si tras abrir el puerto 80 en
> la Security List el sitio no responde, revisar también el firewall del SO
> (`sudo iptables -L` / `netfilter-persistent`).

### Bitácora Red
- [ ] Puerto 22 — solo IPs del equipo
- [ ] Puerto 80 — público
- [ ] Puerto 443 — público (si hay TLS)
- [ ] 8080/8000/5432 — confirmados NO expuestos

---

## Parte 4 — Instalar Docker y desplegar

Ya en la VM (por SSH):

1. Instalar Docker + compose (una sola vez):
   ```bash
   git clone https://github.com/No-Country-simulation/G9-LATAM-Team-36.git
   cd G9-LATAM-Team-36
   bash infra/scripts/setup_vm.sh
   # cerrar sesión SSH y volver a entrar (para usar docker sin sudo)
   ```
2. Configurar el entorno:
   ```bash
   cp .env.example .env
   # editar .env: POSTGRES_PASSWORD real, y si el modelo va por OCI:
   #   USE_LOCAL_MODEL=false + OCI_NAMESPACE / OCI_BUCKET / OCI_REGION
   ```
3. Desplegar (trae cambios, construye, levanta y verifica):
   ```bash
   bash infra/scripts/deploy.sh
   ```
4. Comprobar desde tu máquina que la URL pública responde:
   ```bash
   curl http://<IP_PUBLICA>/          # frontend
   curl http://<IP_PUBLICA>:8000/health   # (solo si expusiste 8000; normalmente NO)
   ```

### Bitácora despliegue
- [ ] Docker instalado en la VM (`setup_vm.sh`)
- [ ] `.env` configurado en la VM (con contraseña real)
- [ ] `deploy.sh` corrió y `check.sh` dio todo verde
- [ ] URL pública verificada: `http://_______/`

---

## Actualizaciones posteriores

Cada vez que haya cambios en `dev`/`main` que quieran llevar a producción:
```bash
ssh ubuntu@<IP_PUBLICA>
cd G9-LATAM-Team-36 && bash infra/scripts/deploy.sh
```

## Día del Demo
- [ ] Correr `bash infra/scripts/check.sh <IP_PUBLICA>` **1 hora antes** del Demo Day.
- [ ] Tener listo el video de respaldo del flujo completo.
