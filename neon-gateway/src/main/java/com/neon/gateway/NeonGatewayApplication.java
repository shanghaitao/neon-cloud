package com.neon.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NeonGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeonGatewayApplication.class, args);
	}

}
