package com.example.hs2actors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Hs2ActorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(Hs2ActorsApplication.class, args);
    }
}
