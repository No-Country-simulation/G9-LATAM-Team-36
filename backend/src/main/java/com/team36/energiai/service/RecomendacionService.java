package com.team36.energiai.service;

import com.team36.energiai.dto.AnalisisRequest;
import com.team36.energiai.model.Categoria;

import java.util.List;

public interface RecomendacionService {
    List<String> generar(AnalisisRequest request, Categoria categoria);
}
