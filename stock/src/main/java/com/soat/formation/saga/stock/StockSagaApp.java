package com.soat.formation.saga.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class StockSagaApp {


    public static void main(String[] args) {
        SpringApplication.run(StockSagaApp.class, args);
    }

}
