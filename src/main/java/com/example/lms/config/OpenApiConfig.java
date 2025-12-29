package com.example.lms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация OpenAPI (метаданные для Swagger UI).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lmsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("LMS API")
                        .version("1.0.0")
                        .description("LMS API: Users, Courses, Enrollments"));
    }
}
