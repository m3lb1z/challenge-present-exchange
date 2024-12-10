package dev.emrx.gitfexchange.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components()
                .addSecuritySchemes("bearer-key",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")))
            .info(new Info()
                .title("Gift Exchange API")
                .description("API para registrar participantes en el juego de intercambio de regalos por Nochebuena. " +
                             "Utiliza roles y credenciales para acceder a los diferentes endpoints de la API.")
                .contact(new Contact()
                        .name("Equipo de desarrollo")
                        .email("support@gitfexchange.com"))
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://gitfexchange.com/api/license"))
                .version("1.0.0")
            );
    }
}
