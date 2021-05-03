package com.soat.formation.saga.orchestrateur;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties()
public class OrchestrateurApp {

        public static void main(String[] args) {
            SpringApplication.run(OrchestrateurApp.class, args);
        }

}
