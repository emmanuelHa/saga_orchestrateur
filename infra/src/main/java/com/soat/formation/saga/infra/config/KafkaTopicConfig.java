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

    // no need many partitions per topic because we use different group
    // and we don't really need to keep order of messages so ...
    private int num3PartitionsForOrder = 3;
    private int num3PartitionsForPayment = 3;
    private int num3PartitionsForStock = 3;
    private int num3PartitionsForBilling = 3;
    private int num3PartitionForDelivery = 3;
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
        return new NewTopic("order", num3PartitionsForOrder, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicStock() {
        return new NewTopic("stock", num3PartitionsForStock, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicPayment() {
        return new NewTopic("payment", num3PartitionsForPayment, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicBilling() {
        return new NewTopic("billing", num3PartitionsForBilling, (short) replicationFactorOrder_1);
    }
    @Bean
    public NewTopic topicDelivery() {
        return new NewTopic("delivery", num3PartitionForDelivery, (short) replicationFactorOrder_1);
    }
}
