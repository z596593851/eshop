package com.hxm.eshop.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients(basePackages = "com.hxm.eshop.auth.feign")
@EnableDiscoveryClient
@EnableRedisHttpSession
@SpringBootApplication
public class EshopAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopAuthApplication.class, args);
    }

}
