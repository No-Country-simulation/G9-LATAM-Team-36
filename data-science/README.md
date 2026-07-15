# Data Science — EnergiAI

Dueños: Bloques A (dataset), B (EDA y criterios), C (modelado)

## Setup
```bash
cd data-science
python -m venv .venv && source .venv/bin/activate
pip install -r requirements.txt
```

## Orden de trabajo
1. `src/generate_dataset.py` → genera `data/dataset_energia.csv` (Bloque A)
2. `notebooks/01_eda_y_criterios.ipynb` → EDA + fórmula de clasificación (Bloque B)
3. `src/labeling_rules.py` → aplica la fórmula del Bloque B sobre el dataset (A + B)
4. `notebooks/02_entrenamiento_modelos.ipynb` → entrena y evalúa (Bloque C)
5. `src/train.py` → versión script del entrenamiento final (Bloque C)
6. `src/upload_model.py` → sube `models/modelo.joblib` a OCI Object Storage (Bloque C)

Ver detalle completo de cada paso en el checklist de Notion.
