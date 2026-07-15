package com.team36.energiai.repository;

import com.team36.energiai.model.Analisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Bloque G — Acceso a datos de la tabla `analisis`. */
@Repository
public interface AnalisisRepository extends JpaRepository<Analisis, Long> {
    // TODO (Bloque G): agregar consultas custom si se necesitan
    // (ej. findByCategoriaOrderByCreadoEnDesc)
}
