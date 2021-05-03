package com.soat.formation.saga.clientui;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(scanBasePackages={"com.soat.formation.saga"})
@SpringBootApplication
@EnableConfigurationProperties
//@EntityScan("com.soat.formation.saga.payment.domain.model")
@ComponentScan({"com.soat.formation.saga.clientui",
        "com.soat.formation.saga.infra.config"})
public class ClientUIApp {


    public static void main(String[] args) {
        /*ApplicationContext ctx = SpringApplication.run(ClientUIApp.class, args);
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }*/
        SpringApplication.run(ClientUIApp.class, args);
    }

}
