package com.hxm.eshop.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hxm.eshop.ware.feign")
@MapperScan("com.hxm.eshop.ware.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class EshopWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopWareApplication.class, args);
    }

}
