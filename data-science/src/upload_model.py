"""
Bloque C — Sube el modelo entrenado a OCI Object Storage.

Requiere tener configurado el OCI CLI config file (~/.oci/config) o
variables de entorno equivalentes. Coordinar el nombre del bucket con
el Bloque J (sugerido: "energiai-models").

Ejecutar:
    python src/upload_model.py
"""
import oci

BUCKET_NAME = "energiai-models"
OBJECT_NAME = "modelo.joblib"
LOCAL_PATH = "data-science/models/modelo.joblib"


def main():
    config = oci.config.from_file()  # usa ~/.oci/config
    object_storage = oci.object_storage.ObjectStorageClient(config)
    namespace = object_storage.get_namespace().data

    with open(LOCAL_PATH, "rb") as f:
        object_storage.put_object(namespace, BUCKET_NAME, OBJECT_NAME, f)

    print(f"Modelo subido a bucket '{BUCKET_NAME}' como '{OBJECT_NAME}'")


if __name__ == "__main__":
    main()
