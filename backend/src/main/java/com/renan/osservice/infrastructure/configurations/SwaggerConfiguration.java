package com.renan.osservice.infrastructure.configurations;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("OS System - API de Chamados")
                        .description("Documentação interativa da API do sistema de Ordens de Serviço. \n\n" +
                                "Essa API permite gerenciar chamados, usuários, técnicos, clientes e estatísticas.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Renan Santana")
                                .email("renanstds@gmail.com")
                                .url("https://github.com/renanstds"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                );
    }
}
