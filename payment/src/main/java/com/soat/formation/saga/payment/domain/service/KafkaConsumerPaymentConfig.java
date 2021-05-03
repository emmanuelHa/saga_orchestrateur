package com.soat.formation.saga.payment.domain.service;

import com.soat.formation.saga.messages.application.commands.AcceptPayment;
import com.soat.formation.saga.messages.application.commands.CancelPayment;
import com.soat.formation.saga.messages.application.commands.RefusePayment;
import com.soat.formation.saga.messages.application.events.CreatePayment;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

@EnableKafka
@Configuration
@ComponentScan("com.soat.formation.saga.infra.config")
public class KafkaConsumerPaymentConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Autowired
    @Qualifier("infraConsumerJsonProps")
    private Map<String, Map<String, Object>> infraJsonProps;


    @Bean
    public ConsumerFactory<String, CreatePayment> consumerFactoryCreatePayment() {
        Map<String, Object> props = infraJsonProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CreatePayment> createPaymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CreatePayment> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryCreatePayment());
        factory.setRecordFilterStrategy(record -> !CreatePayment.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, AcceptPayment> consumerFactoryAcceptPayment() {
        Map<String, Object> props = infraJsonProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AcceptPayment> acceptPaymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, AcceptPayment> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryAcceptPayment());
        factory.setRecordFilterStrategy(record -> !AcceptPayment.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CancelPayment> consumerFactoryCancelPayment() {
        Map<String, Object> props = infraJsonProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CancelPayment> cancelPaymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, CancelPayment> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryCancelPayment());
        factory.setRecordFilterStrategy(record -> !CancelPayment.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, RefusePayment> consumerFactoryRefusePayment() {
        Map<String, Object> props = infraJsonProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "payment");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RefusePayment> refusePaymentKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, RefusePayment> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryRefusePayment());
        factory.setRecordFilterStrategy(record -> !RefusePayment.class.getSimpleName().equals(record.key()));
        return factory;
    }

}
