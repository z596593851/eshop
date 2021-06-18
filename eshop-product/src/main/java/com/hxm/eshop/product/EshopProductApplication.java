package com.hxm.eshop.product;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients(basePackages = "com.hxm.eshop.product.feign")
@MapperScan("com.hxm.eshop.product.dao")
@SpringBootApplication
@EnableRedisHttpSession
@EnableDiscoveryClient
public class EshopProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopProductApplication.class, args);
    }

}
