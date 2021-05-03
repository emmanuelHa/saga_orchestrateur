package com.soat.formation.saga.delivery.config;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.DeliveryCancelled;
import com.soat.formation.saga.messages.application.events.DeliveryPrepared;
import com.soat.formation.saga.messages.application.events.DeliveryStarted;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaDeliveryProducerConfig {

    @Bean
    AbstractKafkaGenericProducer<DeliveryStarted> deliveryStartedProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<DeliveryCancelled> deliveryCancelledProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<DeliveryPrepared> deliveryPreparedProducer() {
        return new KafkaGenericProducer<>();
    }

}
