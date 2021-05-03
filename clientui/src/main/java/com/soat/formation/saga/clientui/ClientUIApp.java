package com.soat.formation.saga.clientui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"com.soat.formation.saga.clientui",
        "com.soat.formation.saga.infra.config"})
public class ClientUIApp {

    public static void main(String[] args) {
        SpringApplication.run(ClientUIApp.class, args);
    }

}
