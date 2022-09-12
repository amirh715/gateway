package com.pmt.gateway.auth.filter;

import com.pmt.gateway.auth.jwt.DecodedToken;
import com.pmt.gateway.auth.jwt.Jwt;
import com.pmt.gateway.auth.routeValidator.RouteValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class JwtAuthenticationFilter implements GatewayFilter {

    @Autowired
    private Jwt jwt;

    @Autowired
    private RouteValidator routeValidator;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        final ServerHttpRequest request = exchange.getRequest();
        final HttpHeaders httpHeaders = request.getHeaders();
        final String authHeader = httpHeaders.get(HttpHeaders.AUTHORIZATION).get(0);

        if(routeValidator.isSecured.test(request)) {
            if(authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            } else {
                final String authToken = authHeader.replace("Bearer ", "");
                final DecodedToken decodedToken = jwt.decode(authToken);
                final String subjectId = decodedToken.getSubjectId();
                final String role = decodedToken.getRole();
                exchange.getRequest()
                        .mutate()
                        .header("subjectId", subjectId)
                        .header("role", role);
                // TODO: bad practice (refactor)
                // if request is for customer service, read customerId query string and populate as a request header.
            }
        }

        return chain.filter(exchange);
    }

}
