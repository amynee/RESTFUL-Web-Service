package com.example.restful_web_service.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiDoc {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Documentation")
                        .version("1.0")
                        .description("Web Service")
                        .contact(new Contact()
                                .name("Amine TLIJANI")
                                .email("tlijaniamine9@gmail.com")
                                .url("https://example.com"))
                );
    }
}
