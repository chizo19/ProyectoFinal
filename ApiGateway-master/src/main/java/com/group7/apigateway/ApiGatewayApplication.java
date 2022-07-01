package com.group7.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/accounts/**")
                        .uri("lb://AccountsService"))
                .route(p -> p.path("/clients/**")
                        .uri("lb://ClientsService"))
                .route(p -> p.path("/credits/**")
                        .uri("lb://CreditsService"))
                .build();
    }
}
