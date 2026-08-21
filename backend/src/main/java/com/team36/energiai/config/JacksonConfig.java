package com.team36.energiai.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.cfg.CoercionAction;
import tools.jackson.databind.cfg.CoercionInputShape;
import tools.jackson.databind.cfg.EnumFeature;
import tools.jackson.databind.type.LogicalType;

@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapperBuilderCustomizer coercionCustomizer() {
        return builder -> builder
                // Enum: rechaza strings ("1") y enteros (1)
                .enable(EnumFeature.FAIL_ON_NUMBERS_FOR_ENUMS)

                // Boolean: rechaza enteros (1, 0) Y strings ("true", "1")
                .withCoercionConfig(LogicalType.Boolean, config -> config
                        .setCoercion(CoercionInputShape.Integer, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.String, CoercionAction.Fail))

                // Integer: rechaza decimales (2.4) Y strings ("2")
                .withCoercionConfig(LogicalType.Integer, config -> config
                        .setCoercion(CoercionInputShape.Float, CoercionAction.Fail)
                        .setCoercion(CoercionInputShape.String, CoercionAction.Fail))

                // Float/Double: rechaza strings ("1.5")
                .withCoercionConfig(LogicalType.Float, config -> config
                        .setCoercion(CoercionInputShape.String, CoercionAction.Fail));

    }
}