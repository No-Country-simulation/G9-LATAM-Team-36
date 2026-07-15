package com.team36.energiai.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

/** Bloque G — Entidad de persistencia según el Contrato 4. */
@Entity
@Table(name = "analisis")
public class Analisis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double consumoKwh;
    private Boolean usoHorarioPico;
    private Integer cantidadEquipos;
    private String tipoInmueble;
    private Integer horasAltoConsumo;

    private String categoria;
    private Double probabilidad;
    private BigDecimal costoEstimadoMensual;

    @Column(columnDefinition = "TEXT")
    private String recomendaciones; // TODO (Bloque G): serializar como JSON (lista -> string)

    private Instant creadoEn = Instant.now();

    protected Analisis() {}

    // TODO (Bloque G): agregar constructor completo, getters/setters
    // (o usar Lombok @Getter/@Setter si el equipo lo prefiere).

    public Long getId() { return id; }
    public String getCategoria() { return categoria; }
    public Instant getCreadoEn() { return creadoEn; }
}
