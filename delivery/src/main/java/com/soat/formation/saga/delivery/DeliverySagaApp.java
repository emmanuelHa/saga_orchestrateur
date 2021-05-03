package com.soat.formation.saga.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class DeliverySagaApp {


    public static void main(String[] args) {
        SpringApplication.run(DeliverySagaApp.class, args);
    }

}
