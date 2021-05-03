package com.soat.formation.saga.infra.config;

import com.soat.formation.saga.messages.application.events.Event;

import java.util.Map;

public class KafkaGenericProducer<T extends Event> extends AbstractKafkaGenericProducer<T> {

    public KafkaGenericProducer<T> mapEventTypeToTopic(Class<T> eventClass, String topicName) {
        this.mapEventClassToTopic((Class<T> eventTypeClass)
                                          -> Map.of(eventClass, topicName).get(eventTypeClass));
        return this;
    }

}
