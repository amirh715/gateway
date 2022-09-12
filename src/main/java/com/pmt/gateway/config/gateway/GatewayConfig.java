package com.pmt.gateway.config.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    GatewayFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("customer-service", r -> r.path("/customer/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://customer-service"))

                .route("account-service", r -> r.path("/account/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://account-service"))
                .build();
    }

}
