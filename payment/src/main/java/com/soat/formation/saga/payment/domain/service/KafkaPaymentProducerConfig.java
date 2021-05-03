package com.soat.formation.saga.payment.domain.service;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.OrderRegistered;
import com.soat.formation.saga.messages.application.events.PaymentAccepted;
import com.soat.formation.saga.messages.application.events.PaymentCancelled;
import com.soat.formation.saga.messages.application.events.PaymentCreated;
import com.soat.formation.saga.messages.application.events.PaymentRefused;
import com.soat.formation.saga.messages.application.events.RegisterOrder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaPaymentProducerConfig {

    @Bean
    AbstractKafkaGenericProducer<PaymentAccepted> paymentAcceptedProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<PaymentCancelled> paymentCancelledProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<PaymentCreated> paymentCreatedProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<PaymentRefused> paymentRefusedProducer() {
        return new KafkaGenericProducer<>();
    }

}
