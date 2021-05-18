package com.neon.gateway.filter;

import com.neon.common.auth.UserToken;
import com.neon.common.util.Consts;
import com.neon.common.util.Jackson;
import com.neon.common.util.Tea;
import lombok.SneakyThrows;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/4/25 9:15
 */
@Component
public class CustomGatewayFilter implements GlobalFilter, Ordered {



    @SneakyThrows
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst(Consts.AUTHORIZED_KEY);

        if (StringUtils.hasText(token)) {
            String jsonStr = Tea.decryptForAppKey(token);

            UserToken userToken = Jackson.JsonToBean(jsonStr, UserToken.class);

            ServerHttpRequest request = exchange.getRequest().mutate().header(Consts.USER_INFO, jsonStr).build();
            return chain.filter(exchange.mutate().request(request).build());
        }

        return chain.filter(exchange);
    }

    public int getOrder() {
        return 0;
    }
}
