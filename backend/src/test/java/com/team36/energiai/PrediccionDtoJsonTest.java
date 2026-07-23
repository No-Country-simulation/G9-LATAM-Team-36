package com.team36.energiai.dto;

import com.team36.energiai.config.JacksonConfig;
import com.team36.energiai.model.Categoria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import org.springframework.context.annotation.Import;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.InvalidFormatException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JsonTest
@Import(JacksonConfig.class)
class PrediccionDtoJsonTest {

    @Autowired
    private ObjectMapper mapper;

    // 1. Accepts valid string values regardless of case
    @ParameterizedTest
    @ValueSource(strings = {"ineficiente", "Ineficiente", "INEFICIENTE"})
    void deserializaCategoriaSinImportarMayusculas(String valor) throws Exception {
        String json = """
            {"categoria": "%s", "probabilidad": 0.81}
            """.formatted(valor);

        PrediccionDto dto = mapper.readValue(json, PrediccionDto.class);

        assertThat(dto.categoria()).isEqualTo(Categoria.INEFICIENTE);
    }

    // 2. Rejects invalid strings
    @Test
    void rechazaStringInvalido() {
        String json = """
            {"categoria": "invalido", "probabilidad": 0.81}
            """;

        assertThatThrownBy(() -> mapper.readValue(json, PrediccionDto.class))
                .isInstanceOf(InvalidFormatException.class);
    }

    // 3. Rejects numeric types (integers/doubles)
    @Test
    void rechazaNumeroComoValorDeEnum() {
        String json = """
        {"categoria": 1, "probabilidad": 0.81}
        """;

        assertThatThrownBy(() -> mapper.readValue(json, PrediccionDto.class))
                .isInstanceOf(InvalidFormatException.class);
    }

    @Test
    void rechazaNullComoCategoria() {
        String json = """
        {"categoria": null, "probabilidad": 0.81}
        """;
        assertThatThrownBy(() -> mapper.readValue(json, PrediccionDto.class))
                .isInstanceOf(tools.jackson.databind.exc.ValueInstantiationException.class)
                .hasCauseInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rechazaJsonMalformado() {
        String json = """
        {"categoria": , "probabilidad": 0.81}
        """;
        assertThatThrownBy(() -> mapper.readValue(json, PrediccionDto.class))
                .isInstanceOf(tools.jackson.core.exc.StreamReadException.class);
    }
}