package com.soat.formation.saga.orchestrateur.config;

import com.soat.formation.saga.messages.application.events.BillingCompleted;
import com.soat.formation.saga.messages.application.events.BillingPrepared;
import com.soat.formation.saga.messages.application.events.DeliveryPrepared;
import com.soat.formation.saga.messages.application.events.DeliveryStarted;
import com.soat.formation.saga.messages.application.events.OrderCreated;
import com.soat.formation.saga.messages.application.events.OrderRegistered;
import com.soat.formation.saga.messages.application.events.PaymentAccepted;
import com.soat.formation.saga.messages.application.events.PaymentCancelled;
import com.soat.formation.saga.messages.application.events.PaymentCreated;
import com.soat.formation.saga.messages.application.events.PaymentRefused;
import com.soat.formation.saga.messages.application.events.StockBooked;
import com.soat.formation.saga.messages.application.events.StockBookingFailed;

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
public class KafkaOrchestrateurConsumerConfig {


    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Autowired
    @Qualifier("infraConsumerJsonProps")
    private Map<String, Map<String, Object>> jsonConsumerProps;

    @Bean
    public ConsumerFactory<String, OrderCreated> orderCreatedKafkaListenerContainerFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreated> orderCreatedListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderCreated> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(orderCreatedKafkaListenerContainerFactory());
        factory.setRecordFilterStrategy(record -> !OrderCreated.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, OrderRegistered> consumerOrderRegisteredFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderRegistered> orderRegisteredKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OrderRegistered> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerOrderRegisteredFactory());
        factory.setRecordFilterStrategy(record -> !OrderRegistered.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, StockBooked> consumerStockBookedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "stock");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockBooked> stockBookedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, StockBooked> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerStockBookedFactory());
        factory.setRecordFilterStrategy(record -> !StockBooked.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BillingCompleted> consumerBillingCompletedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BillingCompleted> billingCompletedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, BillingCompleted> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerBillingCompletedFactory());
        factory.setRecordFilterStrategy(record -> !BillingCompleted.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentRefused> consumerPaymentRefusedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentRefused> paymentRefusedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PaymentRefused> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPaymentRefusedFactory());
        factory.setRecordFilterStrategy(record -> !PaymentRefused.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, DeliveryPrepared> consumerFactoryDeliveryPrepared() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "delivery");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryPrepared> deliveryPreparedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DeliveryPrepared> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryDeliveryPrepared());
        factory.setRecordFilterStrategy(record -> !DeliveryPrepared.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, DeliveryStarted> consumerFactoryDeliveryStarted() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "delivery");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DeliveryStarted> deliveryStartedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DeliveryStarted> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryDeliveryStarted());
        factory.setRecordFilterStrategy(record -> !DeliveryStarted.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, BillingPrepared> consumerBillingPreparedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BillingPrepared> billingPreparedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, BillingPrepared> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerBillingPreparedFactory());
        factory.setRecordFilterStrategy(record -> !BillingPrepared.class.getSimpleName().equals(record.key()));
        return factory;
    }


    @Bean
    public ConsumerFactory<String, PaymentCreated> consumerPaymentCreatedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCreated> paymentCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCreated> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPaymentCreatedFactory());
        factory.setRecordFilterStrategy(record -> !PaymentCreated.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentCancelled> consumerPaymentCancelledFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentCancelled> paymentCancelledKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentCancelled> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPaymentCancelledFactory());
        factory.setRecordFilterStrategy(record -> !PaymentCancelled.class.getSimpleName().equals(record.key()));
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentAccepted> consumerPaymentAcceptedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentAccepted> paymentAcceptedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentAccepted> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerPaymentAcceptedFactory());
        factory.setRecordFilterStrategy(record -> !PaymentAccepted.class.getSimpleName().equals(record.key()));
        return factory;
    }


    @Bean
    public ConsumerFactory<String, StockBookingFailed> consumerStockBookingFailedFactory() {
        Map<String, Object> props = jsonConsumerProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "billing");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, StockBookingFailed> stockBookingFailedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, StockBookingFailed> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerStockBookingFailedFactory());
        factory.setRecordFilterStrategy(record -> !StockBookingFailed.class.getSimpleName().equals(record.key()));
        return factory;
    }
}
