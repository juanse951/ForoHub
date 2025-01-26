package com.aluracursos.Foro.Hub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        Info info = new Info()
                .title("Foro Hub API")
                .description("API Rest del ForoHub, que contiene las funcionalidades CRUD necesarias para un óptimo desempeño.Puedes registrar un nuevo usuario (perfil USER) o usar el siguiente para pruebas: forohub@gmail.com , contraseña: 123456@ForoHub (ADMIN) ")
                .contact(new Contact()
                        .name("Juan Sebastian Giraldo Aguirre")
                        .email("juanse951@gmail.com")
                        .url("https://www.linkedin.com/in/juanse951/"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));

        info.addExtension("x-social-media", Map.of(
                "GitHub", "https://github.com/juanse951",
                "Tiktok", "https://www.tiktok.com/@paghaninitv"
        ));

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(info);
    }
}