package com.recupera.item.back.recupera.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Recupera API")
                .description("Documentação da API para itens perdidos")
                .version("0.01")
                .contact(new Contact().name("Carlos Augusto Carneiro")
                    .email("devcarlosaugusto@outlook.com"))
                .license(new License().name("Licencse exclusiva")
                    .url("API não possui licença, é de uso exclusivo do desenvolvedor"))
            )
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
            .components(new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, createAPIKeyScheme()));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
            .name(SECURITY_SCHEME_NAME)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT");
    }

}
