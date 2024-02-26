package com.example.hs2actors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Hs2ActorsApplication {

    public static void main(String[] args) {
        SpringApplication.run(Hs2ActorsApplication.class, args);
    }

}
