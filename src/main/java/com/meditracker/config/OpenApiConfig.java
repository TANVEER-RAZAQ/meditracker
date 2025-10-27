package com.meditracker.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI meditrackerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MediTracker API")
                        .description("Smart hospital management prototype APIs")
                        .version("v0.1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Docs")
                        .url("http://localhost:8080/swagger"));
    }
}