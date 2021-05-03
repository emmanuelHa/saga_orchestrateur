package com.soat.formation.saga.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties()
public class PaymentSagaApp {


    public static void main(String[] args) {
        SpringApplication.run(PaymentSagaApp.class, args);
    }

}
