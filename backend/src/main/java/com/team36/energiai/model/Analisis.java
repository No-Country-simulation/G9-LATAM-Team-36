package com.team36.energiai.model;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.dto.AnalisisResponse;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "analisis")
public class Analisis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double consumoKwh;
    private Boolean usoHorarioPico;
    private Integer cantidadEquipos;

    @Enumerated(EnumType.STRING)
    private TipoInmueble tipoInmueble;

    private Integer horasAltoConsumo;
    private String categoria;
    private BigDecimal probabilidad;
    private BigDecimal costoEstimadoMensual;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> recomendaciones;
    @CreationTimestamp
    private LocalDateTime creadoEn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getConsumoKwh() {
        return consumoKwh;
    }

    public void setConsumoKwh(Double consumoKwh) {
        this.consumoKwh = consumoKwh;
    }

    public Boolean getUsoHorarioPico() {
        return usoHorarioPico;
    }

    public void setUsoHorarioPico(Boolean usoHorarioPico) {
        this.usoHorarioPico = usoHorarioPico;
    }

    public Integer getCantidadEquipos() {
        return cantidadEquipos;
    }

    public void setCantidadEquipos(Integer cantidadEquipos) {
        this.cantidadEquipos = cantidadEquipos;
    }

    public TipoInmueble getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(TipoInmueble tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public Integer getHorasAltoConsumo() {
        return horasAltoConsumo;
    }

    public void setHorasAltoConsumo(Integer horasAltoConsumo) {
        this.horasAltoConsumo = horasAltoConsumo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(BigDecimal probabilidad) {
        this.probabilidad = probabilidad;
    }

    public BigDecimal getCostoEstimadoMensual() {
        return costoEstimadoMensual;
    }

    public void setCostoEstimadoMensual(BigDecimal costoEstimadoMensual) {
        this.costoEstimadoMensual = costoEstimadoMensual;
    }

    public List<String> getRecomendaciones() {
        return recomendaciones;
    }

    public void setRecomendaciones(List<String> recomendaciones) {
        this.recomendaciones = recomendaciones;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public static Analisis desde(AnalisisRequest request, AnalisisResponse response){
        Analisis analisis = new Analisis();
        analisis.setConsumoKwh(request.consumoKwh());
        analisis.setUsoHorarioPico(request.usoHorarioPico());
        analisis.setCantidadEquipos(request.cantidadEquipos());
        analisis.setTipoInmueble(request.tipoInmueble());
        analisis.setHorasAltoConsumo(request.horasAltoConsumo());
        analisis.setCategoria(response.categoria().name());
        analisis.setProbabilidad(BigDecimal.valueOf(response.probabilidad()));
        analisis.setCostoEstimadoMensual(response.costoEstimadoMensual());
        analisis.setRecomendaciones(response.recomendaciones());
        return  analisis;
    }
}
