package com.menmar.gestionTienda.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  private static final String BEARER_SCHEME = "bearerAuth";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("gestionTienda API")
            .description("""
                API REST para la gestión de una tienda de reparación de calzado
                y duplicado de llaves.
                
                **Autenticación:** JWT Bearer. Obtén el token con `POST /auth/login`
                y añádelo como `Authorization: Bearer <token>` en las peticiones protegidas.
                
                **Roles:**
                - `ADMIN` — gestión de usuarios, catálogo y reset de contraseñas.
                - `EMPLEADO` — operativa diaria: tickets, clientes y fotos.
                """)
            .version("1.0.0")
            .contact(new Contact()
                .name("Félix")
                .email("felixmenendezmarques@gmail.com")))
        .addSecurityItem(new SecurityRequirement().addList(BEARER_SCHEME))
        .components(new Components()
            .addSecuritySchemes(BEARER_SCHEME, new SecurityScheme()
                .name(BEARER_SCHEME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Access token obtenido en POST /auth/login")));
  }
}
