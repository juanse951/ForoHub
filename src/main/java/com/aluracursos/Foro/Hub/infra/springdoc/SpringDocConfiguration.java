package com.aluracursos.Foro.Hub.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Foro Hub API")
                        .description("API Rest del ForoHub, que contiene las funcionalidades CRUD necesarias para un optimo desempeño")
                        .contact(new Contact()
                                .name("Juan Sebastian Giraldo Aguirre")
                                .email("juanse951@gmail.com"))
                        .license(new License()
                                .name("Linkedin")
                                .url("https://www.linkedin.com/in/juanse951/")));
    }


}


