package dev.overlax.caloriesmanager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API documentation")
                        .version("1.0")
                        .description("""
                                Calories Manager RESTful application
                                <p><b>Test credentials:</b><br>
                                - user@gmail.com / password<br>
                                - admin@gmail.com / admin</p>
                                """)
                        .contact(new Contact()
                                .email("alexyekymov@gmail.com")))
                .servers(List.of(
                        new Server()
                                .url("https://calories-manager.overlax.pp.ua")
                                .description("Production HTTPS server")))
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")))
                .security(List.of(new SecurityRequirement().addList("basicAuth")));
    }

}
