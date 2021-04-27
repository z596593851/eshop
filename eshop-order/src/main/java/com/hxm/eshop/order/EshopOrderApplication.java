package com.hxm.eshop.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.hxm.eshop.order.feign")
@MapperScan("com.hxm.eshop.order.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class EshopOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopOrderApplication.class, args);
    }

}
