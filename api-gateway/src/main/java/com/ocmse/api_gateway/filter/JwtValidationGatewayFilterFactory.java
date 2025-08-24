package com.ocmse.api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class JwtValidationGatewayFilterFactory extends
        AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder,
                                             @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(authServiceUrl).build();
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/auth/validate-token")
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String,String>>(){})
                    .flatMap(userInfo ->{
                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-User-Email", (String) userInfo.get("email"))
                                .header("X-User-Role", (String) userInfo.get("role"))
                                .header("X-User-Id", (String) userInfo.get("userId"))
                                .build();
                        return chain.filter(exchange.mutate().request(mutatedRequest).build());
                    }) ;
        };
    }
}
