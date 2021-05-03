package com.soat.formation.saga.payment.domain.service;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.commands.AcceptPayment;
import com.soat.formation.saga.messages.application.commands.CancelPayment;
import com.soat.formation.saga.messages.application.commands.RefusePayment;
import com.soat.formation.saga.messages.application.events.CreatePayment;
import com.soat.formation.saga.messages.application.events.PaymentAccepted;
import com.soat.formation.saga.messages.application.events.PaymentCancelled;
import com.soat.formation.saga.messages.application.events.PaymentCreated;
import com.soat.formation.saga.messages.application.events.PaymentRefused;
import com.soat.formation.saga.payment.application.exception.ImpossibleAddPaymentException;
import com.soat.formation.saga.payment.domain.model.Payment;
import com.soat.formation.saga.payment.infra.dao.PaymentRepository;

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
public class PaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final KafkaGenericProducer<PaymentAccepted> kafkaPaymentAcceptedProducer;
    private final KafkaGenericProducer<PaymentCancelled> kafkaPaymentCancelledProducer;
    private final KafkaGenericProducer<PaymentCreated> kafkaPaymentCreatedProducer;
    private final KafkaGenericProducer<PaymentRefused> kafkaPaymentRefusedProducer;


    @Autowired
    public PaymentService(PaymentRepository paymentRepository,
                          AbstractKafkaGenericProducer<PaymentAccepted> kafkaPaymentAcceptedProducer,
                          AbstractKafkaGenericProducer<PaymentCancelled> kafkaPaymentCancelledProducer,
                          AbstractKafkaGenericProducer<PaymentCreated> kafkaPaymentCreatedProducer,
                          AbstractKafkaGenericProducer<PaymentRefused> kafkaPaymentRefusedProducer) {
        this.paymentRepository = paymentRepository;
        this.kafkaPaymentAcceptedProducer = kafkaPaymentAcceptedProducer.mapEventTypeToTopic(PaymentAccepted.class, "payment");
        this.kafkaPaymentCancelledProducer = kafkaPaymentCancelledProducer.mapEventTypeToTopic(PaymentCancelled.class, "payment");
        this.kafkaPaymentCreatedProducer = kafkaPaymentCreatedProducer.mapEventTypeToTopic(PaymentCreated.class, "payment");
        this.kafkaPaymentRefusedProducer = kafkaPaymentRefusedProducer.mapEventTypeToTopic(PaymentRefused.class, "payment");
    }


    @KafkaListener(topics = "payment", groupId = "payment-createPayment", containerFactory="createPaymentKafkaListenerContainerFactory")
    @Transactional
    public void consume(CreatePayment createPayment) {
        LOGGER.info(String.format("Consumed createPayment %s", createPayment));
        try {
            Payment payment = createPayment(createPayment);
            UUID transactionId = createPayment.getTransactionId();
            PaymentCreated paymentCreated = new PaymentCreated(transactionId, payment.getAmount(), createPayment.getQuantity());
            LOGGER.info(String.format("************ Sending paymentCreated with event id %s", transactionId));
            kafkaPaymentCreatedProducer.send(paymentCreated);
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter ce payment");
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce payment");
        }
    }

    @KafkaListener(topics = "payment", groupId = "payment-acceptPayment", containerFactory="acceptPaymentKafkaListenerContainerFactory")
    @Transactional
    public void consume(AcceptPayment acceptPayment) {
        LOGGER.info(String.format("Consumed AcceptPayment %s", acceptPayment));
        try {
            UUID transactionId = acceptPayment.getTransactionId();
            Payment payment = paymentRepository.findOneByTransactionId(transactionId.toString());

            if(payment == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ce paiment n'a pas été trouvé");
            }
            payment.accept();
            paymentRepository.save(payment);
            PaymentAccepted paymentAccepted = new PaymentAccepted(transactionId, payment.getAmount());
            LOGGER.info(String.format("************ Sending paymentAccepted with event id %s", transactionId));
            kafkaPaymentAcceptedProducer.send(paymentAccepted);
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter ce payment");
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce payment");
        }
    }

    @KafkaListener(topics = "payment", groupId = "payment-cancelPayment", containerFactory="cancelPaymentKafkaListenerContainerFactory")
    @Transactional
    public void consume(CancelPayment cancelPayment) {
        LOGGER.info(String.format("Consumed AcceptPayment %s", cancelPayment));
        try {
            UUID transactionId = cancelPayment.getTransactionId();
            Payment payment = paymentRepository.findOneByTransactionId(transactionId.toString());

            if(payment == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ce paiment n'a pas été trouvé");
            }
            paymentRepository.delete(payment);
            PaymentCancelled paymentCancelled = new PaymentCancelled(transactionId, payment.getAmount());
            LOGGER.info(String.format("************ Sending paymentCancelled with event id %s", transactionId));
            kafkaPaymentCancelledProducer.send(paymentCancelled);
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter ce payment");
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce payment");
        }
    }

    @KafkaListener(topics = "payment", groupId = "payment-refusePayment", containerFactory="refusePaymentKafkaListenerContainerFactory")
    @Transactional
    public void consume(RefusePayment refusePayment) {
        LOGGER.info(String.format("Consumed RefusePayment %s ", refusePayment));
        try {
            UUID transactionId = refusePayment.getTransactionId();
            Payment payment = paymentRepository.findOneByTransactionId(transactionId.toString());
            if(payment == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ce paiment n'a pas été trouvé");
            }
            payment.refuse();
            paymentRepository.save(payment);
            PaymentRefused paymentRefused = new PaymentRefused(transactionId, payment.getAmount(), payment.getQuantity());
            LOGGER.info(String.format("************* Sending paymentRefused with event id %s", transactionId));
            kafkaPaymentRefusedProducer.send(paymentRefused);
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter ce payment");
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce payment");
        }
    }

    @Transactional
    public String save(Payment payment) {
        LOGGER.info("saving Initial Payment " + payment);
        paymentRepository.save(payment);
        return payment.getTransactionId();
    }

    private Payment createPayment(CreatePayment createPayment) {
        UUID transactionId = createPayment.getTransactionId();
        Payment payment = new Payment(transactionId.toString(), createPayment.getAmount(), createPayment.getQuantity());
        paymentRepository.save(payment);
        return payment;
    }

}
