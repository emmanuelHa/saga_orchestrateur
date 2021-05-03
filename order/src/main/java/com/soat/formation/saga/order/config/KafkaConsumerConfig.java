package  com.soat.formation.saga.order.config;

import com.soat.formation.saga.messages.application.events.RegisterOrder;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

// Useless class now ? override group id

@EnableKafka
@Configuration
@ComponentScan("com.soat.formation.saga.infra.config")
@PropertySource("classpath:infra.properties")
public class KafkaConsumerConfig {

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Autowired
    @Qualifier("infraConsumerJsonProps")
    private Map<String, Map<String, Object>> infraJsonProps;


    @Bean
    public ConsumerFactory<String, RegisterOrder> consumerRegisterOrderFactory() {
        Map<String, Object> props = infraJsonProps.get("infraConsumerJsonProps");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "order");
        return new DefaultKafkaConsumerFactory<>(props);
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RegisterOrder> registerOrderKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RegisterOrder> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerRegisterOrderFactory());
        factory.setRecordFilterStrategy(record -> !RegisterOrder.class.getSimpleName().equals(record.key()));

        return factory;
    }

}
