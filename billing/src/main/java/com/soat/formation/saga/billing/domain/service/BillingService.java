package com.soat.formation.saga.billing.domain.service;

import com.soat.formation.saga.billing.application.exception.ImpossibleAddBillingException;
import com.soat.formation.saga.billing.domain.model.Billing;
import com.soat.formation.saga.billing.infra.dao.BillingRepository;
import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.BillingCancelled;
import com.soat.formation.saga.messages.application.events.BillingCompleted;
import com.soat.formation.saga.messages.application.events.BillingPrepared;
import com.soat.formation.saga.messages.application.events.CancelBilling;
import com.soat.formation.saga.messages.application.events.CompleteBilling;
import com.soat.formation.saga.messages.application.events.PrepareBilling;

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
public class BillingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BillingService.class);


    private final BillingRepository billingRepository;

    private final KafkaGenericProducer<BillingCompleted> kafkaBillingCompletedProducer;
    private final KafkaGenericProducer<BillingCancelled> kafkaBillingCancelledProducer;
    private final KafkaGenericProducer<BillingPrepared> kafkaBillingPreparedProducer;


    @Autowired
    public BillingService(BillingRepository billingRepository,
                          AbstractKafkaGenericProducer<BillingCompleted> kafkaBillingCompletedProducer,
                          AbstractKafkaGenericProducer<BillingCancelled> kafkaBillingCancelledProducer,
                          AbstractKafkaGenericProducer<BillingPrepared> kafkaBillingPreparedProducer) {
        this.billingRepository = billingRepository;
        this.kafkaBillingCompletedProducer = kafkaBillingCompletedProducer.mapEventTypeToTopic(BillingCompleted.class, "billing");
        this.kafkaBillingCancelledProducer = kafkaBillingCancelledProducer.mapEventTypeToTopic(BillingCancelled.class, "billing");
        this.kafkaBillingPreparedProducer = kafkaBillingPreparedProducer.mapEventTypeToTopic(BillingPrepared.class, "billing");
    }


    @KafkaListener(topics = "billing", groupId = "billing-cancelBilling", containerFactory="cancelBillingListenerContainerFactory")
    @Transactional
    public void consume(CancelBilling cancelBilling) {
        LOGGER.info(String.format("Consumed cancelBilling %s ", cancelBilling));
        try {
            UUID transactionId = cancelBilling.getTransactionId();
            Billing billing = billingRepository.findByTransactionId(transactionId.toString());
            if(billing == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CancelBilling cette facture n'a pas été trouvée");
            }
            billingRepository.delete(billing);
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter ce payment" + ex);
            throw new ImpossibleAddBillingException("Impossible d'ajouter cette facture" + ex);
        }
    }

    @KafkaListener(topics = "billing", groupId = "billing-completeBilling", containerFactory="completeBillingListenerContainerFactory")
    @Transactional
    public void consume(CompleteBilling completeBilling) {
        LOGGER.info(String.format("Consumed completeBilling %s ", completeBilling));
        try {
            UUID transactionId = completeBilling.getTransactionId();
            Billing billing = billingRepository.findByTransactionId(transactionId.toString());
            if(billing == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "CancelBilling cette facture n'a pas été trouvée");
            }
            billing.isPaid();
            billingRepository.save(billing);
            BillingCompleted billingCompleted = new BillingCompleted(transactionId);
            LOGGER.info(String.format("Sending billingCompleted with event id %s", transactionId));
            kafkaBillingCompletedProducer.send(billingCompleted).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible cette facture" + ex);
            throw new ImpossibleAddBillingException("Impossible d'ajouter cette facture" + ex);
        }
    }

    @KafkaListener(topics = "billing", groupId = "billing-prepareBilling", containerFactory="prepareBillingListenerContainerFactory")
    @Transactional
    public void consume(PrepareBilling prepareBilling) {
        LOGGER.info(String.format("Consumed prepareBilling %s ", prepareBilling));
        try {
            UUID transactionId = prepareBilling.getTransactionId();

            Billing billing = new Billing(transactionId.toString(), prepareBilling.getAddress(),
                                          prepareBilling.getQuantity(), prepareBilling.getAmount());
            billingRepository.save(billing);
            LOGGER.info(String.format("Saved billing with event id %s", transactionId));
            BillingPrepared billingPrepared = new BillingPrepared(transactionId);
            LOGGER.info(String.format("Sending billingPrepared with event id %s", transactionId));
            kafkaBillingPreparedProducer.send(billingPrepared).get();
        }
        catch(Exception ex) {
            LOGGER.error("prepareBilling erreur " + ex);
            throw new ImpossibleAddBillingException("prepareBilling erreur " + ex);
        }
    }
}
