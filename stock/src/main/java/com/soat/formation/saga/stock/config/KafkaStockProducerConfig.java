package com.soat.formation.saga.stock.config;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.StockBooked;
import com.soat.formation.saga.messages.application.events.StockBookingFailed;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStockProducerConfig {

    @Bean
    public AbstractKafkaGenericProducer<StockBooked> newKafkaStockBookedProducer() {
        return new KafkaGenericProducer<>();
    }

    @Bean
    public AbstractKafkaGenericProducer<StockBookingFailed> newKafkaStockBookingFailedProducer() {
        return new KafkaGenericProducer<>();
    }


}
