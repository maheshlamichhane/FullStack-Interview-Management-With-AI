package com.itsutra.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayserverApplication {

	public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
	}

    @Bean
    public RouteLocator interviewRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()

                .route(p -> p
                        .path("/api/interviews/**")
                        .filters(f -> f.rewritePath("/api/interviews/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName("interviewsCircuitBreaker")))
                        .uri("lb://interviews")
                )
                .route(p -> p
                        .path("/api/interviews-ai/**")
                        .filters(f -> f.rewritePath("/api/interviews-ai/(?<segment>.*)", "/${segment}")
                                .circuitBreaker(config -> config.setName("interviewsaicircuitBreaker")))
                        .uri("lb://interviews-ai")
                )
                .build();
    }

}
