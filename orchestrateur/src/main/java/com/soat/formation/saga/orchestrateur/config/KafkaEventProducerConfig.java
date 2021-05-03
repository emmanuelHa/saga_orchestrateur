package com.soat.formation.saga.orchestrateur.config;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.commands.CancelPayment;
import com.soat.formation.saga.messages.application.events.BookStock;
import com.soat.formation.saga.messages.application.events.CancelBilling;
import com.soat.formation.saga.messages.application.events.CancelDelivery;
import com.soat.formation.saga.messages.application.events.CompleteBilling;
import com.soat.formation.saga.messages.application.events.CreatePayment;
import com.soat.formation.saga.messages.application.events.PrepareBilling;
import com.soat.formation.saga.messages.application.events.PrepareDelivery;
import com.soat.formation.saga.messages.application.events.RegisterOrder;
import com.soat.formation.saga.messages.application.events.StartDelivery;
import com.soat.formation.saga.messages.application.events.UnBookStock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaEventProducerConfig {

    @Bean
    AbstractKafkaGenericProducer<RegisterOrder> registerOrderProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<PrepareBilling> prepareBillingProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<CompleteBilling> completeBillingProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<CancelBilling> cancelBillingProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<BookStock> bookStockProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<UnBookStock> unBookStockProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<CreatePayment> createPaymentProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<CancelPayment> cancelPaymentProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<StartDelivery> startDeliveryProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<CancelDelivery> cancelDeliveryProducer() {
        return new KafkaGenericProducer<>();
    }
    @Bean
    AbstractKafkaGenericProducer<PrepareDelivery> prepareDeliveryProducer() {
        return new KafkaGenericProducer<>();
    }

}
