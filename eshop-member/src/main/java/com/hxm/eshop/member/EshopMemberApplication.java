package com.hxm.eshop.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hxm.eshop.member.feign")
@MapperScan("com.hxm.eshop.member.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class EshopMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopMemberApplication.class, args);
    }

}
