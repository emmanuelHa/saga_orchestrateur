package com.soat.formation.saga.infra.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    private int num5PartitionsForOrder = 10;
    private int num5PartitionsForPayment = 5;
    private int num2PartitionsForStock = 2;
    private int num2PartitionsForBilling = 2;
    private int num1PartitionForDelivery = 1;
    // dev environment
    private int replicationFactorOrder_1 = 1;

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic topicOrder() {
        return new NewTopic("order", num5PartitionsForOrder, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicStock() {
        return new NewTopic("stock", num2PartitionsForStock, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicPayment() {
        return new NewTopic("payment", num5PartitionsForPayment, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicBilling() {
        return new NewTopic("billing", num2PartitionsForBilling, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicDelivery() {
        return new NewTopic("delivery", num1PartitionForDelivery, (short) replicationFactorOrder_1);
    }
}
