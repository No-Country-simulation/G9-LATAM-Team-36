package com.team36.energiai.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Converter
public class RecomendacionesConverter implements AttributeConverter<List<String>, String> {

    private static final ObjectMapper M = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<String> lista) {
        try { return M.writeValueAsString(lista == null ? List.of() : lista); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
    @Override
    public List<String> convertToEntityAttribute(String json) {
        try { return (json == null || json.isBlank())
                ? List.of()
                : M.readValue(json, new TypeReference<List<String>>() {}); }
        catch (Exception e) { throw new IllegalStateException(e); }
    }
}
