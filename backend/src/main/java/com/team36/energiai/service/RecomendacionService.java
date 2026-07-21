package com.team36.energiai.service;

import com.team36.energiai.dto.AnalisisRequest;

import java.util.List;

public interface RecomendacionService {
    List<String> generar(AnalisisRequest req, String categoria);
}
