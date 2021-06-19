package com.hxm.eshop.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients(basePackages = "com.hxm.eshop.cart.feign")
@SpringBootApplication
@EnableRedisHttpSession
@EnableDiscoveryClient
public class EshopCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopCartApplication.class, args);
    }

}
