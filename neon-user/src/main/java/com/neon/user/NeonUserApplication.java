package com.neon.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients("com.neon.*.controller")
public class NeonUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeonUserApplication.class, args);
	}

}
