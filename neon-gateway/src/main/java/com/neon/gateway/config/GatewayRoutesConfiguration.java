package com.neon.gateway.config;

import com.neon.gateway.filter.CustomGatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author shanghaitao
 * @Date 2021/4/25 9:18
 */
@Configuration
public class GatewayRoutesConfiguration {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder){
        return builder.routes().route(r->r
                .path("/getUser**")
                .uri("lb://neon-user")
                .id("neon-user"))
                .build();
    }
}
