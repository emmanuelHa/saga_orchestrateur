package com.soat.formation.saga.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class OrderSagaApp {


    public static void main(String[] args) {
        SpringApplication.run(OrderSagaApp.class, args);
    }

}
