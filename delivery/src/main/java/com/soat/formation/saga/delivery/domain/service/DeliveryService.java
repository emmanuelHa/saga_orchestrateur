package com.soat.formation.saga.delivery.domain.service;

import com.soat.formation.saga.delivery.application.exception.ImpossibleAddDeliveryException;
import com.soat.formation.saga.delivery.domain.model.Delivery;
import com.soat.formation.saga.delivery.infra.dao.DeliveryRepository;
import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.CancelDelivery;
import com.soat.formation.saga.messages.application.events.DeliveryCancelled;
import com.soat.formation.saga.messages.application.events.DeliveryPrepared;
import com.soat.formation.saga.messages.application.events.DeliveryStarted;
import com.soat.formation.saga.messages.application.events.PrepareDelivery;
import com.soat.formation.saga.messages.application.events.StartDelivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import javax.transaction.Transactional;

@Service
public class DeliveryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryService.class);
    private final DeliveryRepository deliveryRepository;
    private final KafkaGenericProducer<DeliveryStarted> kafkaDeliveryStartedProducer;
    private final KafkaGenericProducer<DeliveryCancelled> kafkaDeliveryCanceledProducer;
    private final KafkaGenericProducer<DeliveryPrepared> kafkaDeliveryPreparedProducer;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository,
                           AbstractKafkaGenericProducer<DeliveryStarted> kafkaDeliveryStartedProducer,
                           AbstractKafkaGenericProducer<DeliveryCancelled> kafkaDeliveryCanceledProducer,
                           AbstractKafkaGenericProducer<DeliveryPrepared> kafkaDeliveryPreparedProducer
    ) {
        this.deliveryRepository = deliveryRepository;
        this.kafkaDeliveryStartedProducer = kafkaDeliveryStartedProducer.mapEventTypeToTopic(DeliveryStarted.class, "delivery");
        this.kafkaDeliveryCanceledProducer = kafkaDeliveryCanceledProducer.mapEventTypeToTopic(DeliveryCancelled.class, "delivery");
        this.kafkaDeliveryPreparedProducer = kafkaDeliveryPreparedProducer.mapEventTypeToTopic(DeliveryPrepared.class, "delivery");
    }

    @KafkaListener(topics = "delivery", groupId = "delivery-cancelDelivery", containerFactory="cancelDeliveryListenerContainerFactory")
    @Transactional
    public void consume(CancelDelivery cancelDelivery) {
        LOGGER.info(String.format("Consumed cancelDelivery %s", cancelDelivery));
        try {
            UUID transactionId = cancelDelivery.getTransactionId();
            Delivery delivery = deliveryRepository.findByTransactionId(transactionId.toString());
            if(delivery == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette livraison n'a pas été trouvée");
            }

            LOGGER.info(String.format("Deleting delivery %s", delivery));
            deliveryRepository.delete(delivery);

            DeliveryCancelled deliveryCancelled = new DeliveryCancelled(transactionId);
            LOGGER.info(String.format("Sending deliveryCancelled with event id %s", transactionId));
            kafkaDeliveryCanceledProducer.send(deliveryCancelled).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de faire un cancelDelivery " + ex);
            throw new ImpossibleAddDeliveryException("Impossible de faire un cancelDelivery " + ex);
        }
    }

    @KafkaListener(topics = "delivery", groupId = "delivery-prepareDelivery", containerFactory="prepareDeliveryListenerContainerFactory")
    @Transactional
    public void consume(PrepareDelivery prepareDelivery) {
        LOGGER.info(String.format("Consumed prepareDelivery %s", prepareDelivery));
        try {
            UUID transactionId = prepareDelivery.getTransactionId();
            Delivery delivery = new Delivery(transactionId, prepareDelivery.getAddress());

            deliveryRepository.save(delivery);
            LOGGER.info(String.format("Delivery created %s waiting for Payment, Stock and billing services", delivery));

            DeliveryPrepared deliveryPrepared = new DeliveryPrepared(transactionId);
            LOGGER.info(String.format("Sending deliveryPrepared with transaction id %s", transactionId));
            kafkaDeliveryPreparedProducer.send(deliveryPrepared).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de faire un prepareDelivery " + ex);
            throw new ImpossibleAddDeliveryException("Impossible de faire un prepareDelivery " + ex);
        }
    }

    @KafkaListener(topics = "delivery", groupId = "delivery-startDelivery", containerFactory="startDeliveryListenerContainerFactory")
    @Transactional
    public void consume(StartDelivery startDelivery) {
        LOGGER.info(String.format("Consumed startDelivery %s", startDelivery));
        try {
            UUID transactionId = startDelivery.getTransactionId();
            Delivery delivery = deliveryRepository.findByTransactionId(transactionId.toString());

            if(delivery == null) {
                LOGGER.error("Cette livraison n'a pas été trouvée");
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cette livraison n'a pas été trouvée");
            }
            delivery.ship();
            deliveryRepository.save(delivery);
            LOGGER.info(String.format("Delivery updated %s ", delivery));

            DeliveryStarted deliveryStarted = new DeliveryStarted(transactionId);
            LOGGER.info(String.format("Sending deliveryStarted with transaction id %s", transactionId));
            kafkaDeliveryStartedProducer.send(deliveryStarted).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de faire un startDelivery " + ex);
            throw new ImpossibleAddDeliveryException("Impossible de faire un startDelivery " + ex);
        }
    }
}
