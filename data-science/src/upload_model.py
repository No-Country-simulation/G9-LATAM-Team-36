"""
Bloque C — Sube el modelo entrenado y su metadata a OCI Object Storage.

Sube dos objetos al bucket (por defecto 'energiai-models'):
  - modelo.joblib   (el pipeline serializado que consume el Bloque D)
  - metadata.json   (trazabilidad: fecha, metricas, version de sklearn)

Requiere un OCI config file (~/.oci/config) o las variables de entorno
equivalentes. Coordinar el nombre del bucket con el Bloque J.

Ejecutar desde la raíz del repo:
    python data-science/src/upload_model.py            # sube a OCI
    python data-science/src/upload_model.py --dry-run  # valida sin subir

Variables de entorno opcionales:
    OCI_BUCKET           nombre del bucket (default: energiai-models)
    OCI_CONFIG_PROFILE   perfil del config file (default: DEFAULT)
"""
import argparse
import os
import sys
from pathlib import Path

DIR_MODELS = Path("data-science/models")
ARCHIVOS = ["modelo.joblib", "metadata.json"]

BUCKET_NAME = os.getenv("OCI_BUCKET", "energiai-models")
CONFIG_PROFILE = os.getenv("OCI_CONFIG_PROFILE", "DEFAULT")


def validar_archivos() -> list[Path]:
    """Verifica que existan los artefactos locales antes de intentar subir."""
    rutas = [DIR_MODELS / nombre for nombre in ARCHIVOS]
    faltantes = [str(r) for r in rutas if not r.exists()]
    if faltantes:
        print("ERROR: faltan artefactos locales. Corré primero el entrenamiento:")
        print("  python data-science/src/train.py")
        print("Faltan:", ", ".join(faltantes))
        sys.exit(1)
    return rutas


def main(dry_run: bool = False):
    rutas = validar_archivos()

    if dry_run:
        print("[dry-run] artefactos encontrados:")
        for r in rutas:
            print(f"  - {r} ({r.stat().st_size / 1e6:.2f} MB)")
        print(f"[dry-run] se subirian al bucket '{BUCKET_NAME}' (perfil {CONFIG_PROFILE}). "
              "No se ejecutó ninguna llamada a OCI.")
        return

    import oci

    try:
        config = oci.config.from_file(profile_name=CONFIG_PROFILE)
    except (oci.exceptions.ConfigFileNotFound, oci.exceptions.ProfileNotFound) as e:
        print(f"ERROR: no se pudo cargar el config de OCI ({e}).")
        print("Configurá ~/.oci/config (coordinar con el Bloque J) o usá --dry-run.")
        sys.exit(1)

    client = oci.object_storage.ObjectStorageClient(config)
    namespace = client.get_namespace().data

    for ruta in rutas:
        with open(ruta, "rb") as f:
            client.put_object(namespace, BUCKET_NAME, ruta.name, f)
        print(f"Subido: {ruta.name} -> bucket '{BUCKET_NAME}'")

    print(f"\nListo. Avisar al Bloque D: objetos '{ARCHIVOS[0]}' y '{ARCHIVOS[1]}' "
          f"disponibles en el bucket '{BUCKET_NAME}'.")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Sube el modelo a OCI Object Storage.")
    parser.add_argument("--dry-run", action="store_true",
                        help="Valida los artefactos locales sin subir a OCI.")
    args = parser.parse_args()
    main(dry_run=args.dry_run)
