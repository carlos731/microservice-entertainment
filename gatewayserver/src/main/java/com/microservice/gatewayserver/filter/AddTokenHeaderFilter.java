package com.microservice.gatewayserver.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AddTokenHeaderFilter extends AbstractGatewayFilterFactory<AddTokenHeaderFilter.Config> {

    public static class Config {
        // Configurações do filtro se necessário
    }

    public AddTokenHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst("Authorization"); // Ou de onde você estiver obtendo o token
            if (token != null) {
                exchange = exchange.mutate()
                        .request(r -> r.headers(h -> h.set("Authorization", token))) // Adiciona o token ao cabeçalho
                        .build();
            }
            return chain.filter(exchange);
        };
    }
}
