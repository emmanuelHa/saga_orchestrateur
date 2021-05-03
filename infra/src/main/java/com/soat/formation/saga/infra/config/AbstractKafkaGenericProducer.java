package com.soat.formation.saga.infra.config;

import com.soat.formation.saga.messages.application.events.Event;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@PropertySource("classpath:infra.properties")
public abstract class AbstractKafkaGenericProducer<T extends Event> {

    private final Logger LOGGER = LoggerFactory.getLogger(AbstractKafkaGenericProducer.class);

    @Value(value = "${kafka.bootstrap-servers}")
    private String bootstrapAddress;
    private Function<Class<T>, String> eventClassToTopicFunction;

    abstract public KafkaGenericProducer<T> mapEventTypeToTopic(Class<T> eventClass, String topicName);

    public ListenableFuture<SendResult<String, T>> send(T event) {
        KafkaTemplate<String, T> eventByStringKafkaTemplate = genericJsonKafkaTemplate();
        Class<T> eventClass = (Class<T>) event.getClass();
        String topic = getTopicByEventType(eventClass);
        LOGGER.info(String.format("About to send %s to topic %s", eventClass.getSimpleName(), topic));
        return eventByStringKafkaTemplate.send(topic,
                                              eventClass.getSimpleName(), event);
    }

    public void flush() {
        KafkaTemplate<String, T> eventByStringKafkaTemplate = genericJsonKafkaTemplate();
        eventByStringKafkaTemplate.flush();
    }

    public AbstractKafkaGenericProducer<T> mapEventClassToTopic(Function<Class<T>, String> mapEventClassToTopicFunction) {
        this.eventClassToTopicFunction = mapEventClassToTopicFunction;
        return this;
    }

    private ProducerFactory<String, T> jsonProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private KafkaTemplate<String, T> genericJsonKafkaTemplate() {
        return new KafkaTemplate<>(jsonProducerFactory());
    }

    private String getTopicByEventType(Class<T> eventTypeClass) {
        return getMappingEventClassToTopic().apply(eventTypeClass);
    }

    private Function<Class<T>, String> getMappingEventClassToTopic() {
        return eventClassToTopicFunction;
    }

}
