package com.pyonsnalcolor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("bearerAuth");

        SecurityScheme bearerRef = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("bearerRef");

        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("Authorization");
        addSecurityItem.addList("Refresh");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Authorization", bearerAuth)
                        .addSecuritySchemes("Refresh", bearerRef))
                .addSecurityItem(addSecurityItem)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Pyonsnal Color")
                .description("Pyonsnal Color API 문서")
                .version("1.0.0");
    }
}

// http://localhost:8080/swagger-ui/index.html