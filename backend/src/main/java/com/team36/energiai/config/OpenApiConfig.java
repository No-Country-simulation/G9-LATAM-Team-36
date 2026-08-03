package com.team36.energiai.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI energiaiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("EnergiAI API")
                        .description("API de analisis de eficiencia energetica - Hackathon G9 Alura + Oracle")
                        .version("v1")
                        .contact(new Contact().name("Team 36")));
    }
}