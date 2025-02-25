package com.johan.gym_control.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .components(new Components())
        .info(
            new Info()
                .title("Api de FitSync")
                .description(
                    "Documentación interactiva de la API RESTful para gestion de entradas de usuarios a un gimnasio")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Equipo de desarrollo de FitSync ")
                    .email("javilla22@soy.sena.edu.co")));
  }
}
