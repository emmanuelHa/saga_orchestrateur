package com.soat.formation.saga.billing.config;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.BillingCancelled;
import com.soat.formation.saga.messages.application.events.BillingCompleted;
import com.soat.formation.saga.messages.application.events.BillingPrepared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaBillingProducerConfig {

    @Bean
    AbstractKafkaGenericProducer<BillingCompleted> billingCompletedProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<BillingCancelled> billingCancelledProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<BillingPrepared> billingPreparedProducer() {
        return new KafkaGenericProducer<>();
    }

}
