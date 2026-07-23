package com.team36.energiai.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CalculoFinancieroService {

    private final BigDecimal tarifaKwh;

    public CalculoFinancieroService(@Value("${tarifa.kwh}") BigDecimal tarifaKwh) {
        this.tarifaKwh = tarifaKwh;
    }

    public BigDecimal calcularCostoEstimado(Double consumoKwh) {
        return BigDecimal.valueOf(consumoKwh)
                .multiply(tarifaKwh)
                .setScale(2, RoundingMode.HALF_UP);
    }
}