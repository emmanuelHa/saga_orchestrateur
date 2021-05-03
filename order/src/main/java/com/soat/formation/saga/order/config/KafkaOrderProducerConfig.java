package com.soat.formation.saga.order.config;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.OrderRegistered;
import com.soat.formation.saga.messages.application.events.RegisterOrder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaOrderProducerConfig {

    @Bean
    AbstractKafkaGenericProducer<RegisterOrder> registerOrderProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<OrderRegistered> orderRegisteredProducer() {
        return new KafkaGenericProducer<>();
    }

}
