package com.soat.formation.saga.orchestrateur.service;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.commands.CancelPayment;
import com.soat.formation.saga.messages.application.events.BillingCompleted;
import com.soat.formation.saga.messages.application.events.BillingPrepared;
import com.soat.formation.saga.messages.application.events.BookStock;
import com.soat.formation.saga.messages.application.events.CancelBilling;
import com.soat.formation.saga.messages.application.events.CancelDelivery;
import com.soat.formation.saga.messages.application.events.CompleteBilling;
import com.soat.formation.saga.messages.application.events.CreatePayment;
import com.soat.formation.saga.messages.application.events.DeliveryPrepared;
import com.soat.formation.saga.messages.application.events.DeliveryStarted;
import com.soat.formation.saga.messages.application.events.OrderCreated;
import com.soat.formation.saga.messages.application.events.OrderRegistered;
import com.soat.formation.saga.messages.application.events.PaymentAccepted;
import com.soat.formation.saga.messages.application.events.PaymentCancelled;
import com.soat.formation.saga.messages.application.events.PaymentCreated;
import com.soat.formation.saga.messages.application.events.PaymentRefused;
import com.soat.formation.saga.messages.application.events.PrepareBilling;
import com.soat.formation.saga.messages.application.events.PrepareDelivery;
import com.soat.formation.saga.messages.application.events.RegisterOrder;
import com.soat.formation.saga.messages.application.events.StartDelivery;
import com.soat.formation.saga.messages.application.events.StockBooked;
import com.soat.formation.saga.messages.application.events.StockBookingFailed;
import com.soat.formation.saga.messages.application.events.UnBookStock;
import com.soat.formation.saga.messages.application.exceptions.ImpossibleAddOrderException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class SagaExecutionCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SagaExecutionCoordinator.class);

    private final KafkaGenericProducer<RegisterOrder> kafkaRegisterOrderProducer;

    private KafkaGenericProducer<PrepareDelivery> kafkaPrepareDeliveryProducer;
    private KafkaGenericProducer<PrepareBilling> kafkaPrepareBillingProducer;
    private KafkaGenericProducer<CompleteBilling> kafkaCompleteBillingProducer;
    private KafkaGenericProducer<BookStock> kafkaBookStockProducer;
    private KafkaGenericProducer<CreatePayment> kafkaCreatePaymentProducer;
    private KafkaGenericProducer<StartDelivery> kafkaStartDeliveryProducer;
    private KafkaGenericProducer<CancelDelivery> kafkaCancelDeliveryProducer;
    private final KafkaGenericProducer<CancelBilling> kafkaCancelBillingProducer;
    private final KafkaGenericProducer<UnBookStock> kafkaUnbookStockProducer;
    private final KafkaGenericProducer<CancelPayment> kafkaCancelPaymentProducer;

    private Integer quantity;
    private String address;
    private float amount;
    private boolean registered;
    private boolean stockBooked;
    private boolean billingCompleted;
    private boolean paymentAccepted;
    private boolean cancelled;
    private boolean shipped;
    private boolean deliveryPrepared;
    private boolean billingPrepared;
    private boolean paymentCreated;


    @Autowired
    public SagaExecutionCoordinator(AbstractKafkaGenericProducer<RegisterOrder> kafkaRegisterOrderProducer,
                                    AbstractKafkaGenericProducer<PrepareBilling> kafkaPrepareBillingProducer,
                                    AbstractKafkaGenericProducer<CompleteBilling> kafkaCompleteBillingProducer,
                                    AbstractKafkaGenericProducer<CancelBilling> kafkaCancelBillingProducer,
                                    AbstractKafkaGenericProducer<BookStock> kafkaBookStockProducer,
                                    AbstractKafkaGenericProducer<UnBookStock> kafkaUnbookStockProducer,
                                    AbstractKafkaGenericProducer<CreatePayment> kafkaCreatePaymentProducer,
                                    AbstractKafkaGenericProducer<CancelPayment> kafkaCancelPaymentProducer,
                                    AbstractKafkaGenericProducer<StartDelivery> kafkaStartDeliveryProducer,
                                    AbstractKafkaGenericProducer<CancelDelivery> kafkaCancelDeliveryProducer,
                                    AbstractKafkaGenericProducer<PrepareDelivery> kafkaPrepareDeliveryProducer
    ) {
        this.kafkaRegisterOrderProducer = kafkaRegisterOrderProducer.mapEventTypeToTopic(RegisterOrder.class, "order");
        this.kafkaPrepareDeliveryProducer = kafkaPrepareDeliveryProducer.mapEventTypeToTopic(PrepareDelivery.class,"delivery");
        this.kafkaStartDeliveryProducer = kafkaStartDeliveryProducer.mapEventTypeToTopic(StartDelivery.class,"delivery");
        this.kafkaCancelDeliveryProducer = kafkaCancelDeliveryProducer.mapEventTypeToTopic(CancelDelivery.class,"delivery");
        this.kafkaCompleteBillingProducer = kafkaCompleteBillingProducer.mapEventTypeToTopic(CompleteBilling.class,"billing");
        this.kafkaPrepareBillingProducer = kafkaPrepareBillingProducer.mapEventTypeToTopic(PrepareBilling.class,"billing");
        this.kafkaCancelBillingProducer = kafkaCancelBillingProducer.mapEventTypeToTopic(CancelBilling.class,"billing");
        this.kafkaUnbookStockProducer = kafkaUnbookStockProducer.mapEventTypeToTopic(UnBookStock.class,"stock");
        this.kafkaBookStockProducer = kafkaBookStockProducer.mapEventTypeToTopic(BookStock.class,"stock");
        this.kafkaCreatePaymentProducer = kafkaCreatePaymentProducer.mapEventTypeToTopic(CreatePayment.class,"payment");
        this.kafkaCancelPaymentProducer = kafkaCancelPaymentProducer.mapEventTypeToTopic(CancelPayment.class,"payment");
    }

    @KafkaListener(topics = "order", groupId = "coordinator-orderCreated", containerFactory="orderCreatedListenerContainerFactory")
    @Transactional
    public void consume(OrderCreated orderCreated) {
        LOGGER.info(String.format("Consumed message: %s", orderCreated));
        try {
            UUID transactionId = orderCreated.getTransactionId();
            this.quantity = orderCreated.getQuantity();
            this.address = orderCreated.getAddress();
            this.amount = orderCreated.getQuantity() * 2.5f;
            RegisterOrder registerOrder = new RegisterOrder(transactionId, address, quantity);
            LOGGER.info(String.format("Sending registerOrder %s waiting for Payment and Stock service", registerOrder));
            kafkaRegisterOrderProducer.send(registerOrder).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible d'ajouter cette command" + ex);
            throw new ImpossibleAddOrderException("Impossible d'ajouter cette commande" + ex);
        }
    }

    @KafkaListener(topics = "order", groupId = "coordinator-orderRegistered", containerFactory="orderRegisteredKafkaListenerContainerFactory")
    @Transactional
    public void consume(OrderRegistered orderRegistered) {
        LOGGER.info(String.format("Consumed orderRegistered: %s", orderRegistered));
        try {
            this.registered = true;
            LOGGER.info("Order service did register");
            PrepareDelivery prepareDelivery = new PrepareDelivery(orderRegistered.getTransactionId(), orderRegistered.getAddress());
            LOGGER.info(String.format("Sending prepareDelivery %s", prepareDelivery));
            kafkaPrepareDeliveryProducer.send(prepareDelivery).get();

            PrepareBilling prepareBilling = new PrepareBilling(orderRegistered.getTransactionId(),
                                                               orderRegistered.getAddress(),
                                                               orderRegistered.getQuantity(),
                                                               orderRegistered.getAmount());
            LOGGER.info(String.format("Sending prepareBilling %s", prepareBilling));
            kafkaPrepareBillingProducer.send(prepareBilling).get();

            BookStock bookStock = new BookStock(orderRegistered.getTransactionId(), orderRegistered.getQuantity());
            LOGGER.info(String.format("Sending bookStock %s", bookStock));
            kafkaBookStockProducer.send(bookStock).get();

            CreatePayment createPayment = new CreatePayment(orderRegistered.getTransactionId(), orderRegistered.getAmount(), orderRegistered.getQuantity());
            LOGGER.info(String.format("Sending createPayment %s", createPayment));
            kafkaCreatePaymentProducer.send(createPayment).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce orderRegistered" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce orderRegistered" + ex);
        }
    }

    @KafkaListener(topics = "stock", groupId = "coordinator-stockBooked", containerFactory="stockBookedKafkaListenerContainerFactory")
    @Transactional
    public void consume(StockBooked stockBooked) {
        LOGGER.info(String.format("Consumed stockBooked: %s", stockBooked));
        try {
            this.stockBooked = true;
            LOGGER.info("Marked stockBooked to true");
            tryStartDelivery(stockBooked.getTransactionId());

        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce stockBooked" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce stockBooked" + ex);
        }
    }

    @KafkaListener(topics = "billing", groupId = "coordinator-billingCompleted", containerFactory="billingCompletedKafkaListenerContainerFactory")
    @Transactional
    public void consume(BillingCompleted billingCompleted) {
        LOGGER.info(String.format("Consumed billingCompleted: %s", billingCompleted));
        try {
            this.billingCompleted = true;
            LOGGER.info("Marked billingCompleted to true");
            tryStartDelivery(billingCompleted.getTransactionId());
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce billingCompleted" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce billingCompleted" + ex);
        }
    }

    @KafkaListener(topics = "payment", groupId = "coordinator-paymentRefused", containerFactory="paymentRefusedKafkaListenerContainerFactory")
    @Transactional
    public void consume(PaymentRefused paymentRefused) {
        LOGGER.info(String.format("Consumed paymentRefused: %s", paymentRefused));
        try {
            this.paymentAccepted = false;
            LOGGER.info("Marked paymentAccepted to false");
            CancelDelivery cancelDelivery = new CancelDelivery(paymentRefused.getTransactionId());
            LOGGER.info(String.format("Sending cancelBilling %s", cancelDelivery));
            kafkaCancelDeliveryProducer.send(cancelDelivery).get();

            CancelBilling cancelBilling = new CancelBilling(paymentRefused.getTransactionId());
            LOGGER.info(String.format("Sending cancelBilling %s", cancelBilling));
            kafkaCancelBillingProducer.send(cancelBilling).get();

            UnBookStock unBookStock = new UnBookStock(paymentRefused.getTransactionId(), paymentRefused.getQuantity());
            LOGGER.info(String.format("Sending unBookStock %s", unBookStock));
            kafkaUnbookStockProducer.send(unBookStock).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce paymentRefused" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce paymentRefused" + ex);
        }
    }

    @KafkaListener(topics = "delivery", groupId = "coordinator-deliveryPrepared", containerFactory="deliveryPreparedKafkaListenerContainerFactory")
    @Transactional
    public void consume(DeliveryPrepared deliveryPrepared) {
        LOGGER.info(String.format("Consumed deliveryPrepared: %s", deliveryPrepared));
        try {
            this.deliveryPrepared = true;
            LOGGER.info("Marked deliveryPrepared to true");
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce deliveryPrepared" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce deliveryPrepared" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("DELIVERY PREPARED");
        LOGGER.info("****************");
    }

    @KafkaListener(topics = "delivery", groupId = "coordinator-deliveryStarted", containerFactory="deliveryStartedKafkaListenerContainerFactory")
    @Transactional
    public void consume(DeliveryStarted deliveryStarted) {
        LOGGER.info(String.format("Consumed deliveryStarted: %s", deliveryStarted));
        try {
            this.shipped = true;
            LOGGER.info("Marked shipped to true");
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce deliveryStarted" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce deliveryStarted" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("SHIPPED");
        LOGGER.info("****************");
    }

    @KafkaListener(topics = "billing", groupId = "coordinator-billingPrepared", containerFactory="billingPreparedKafkaListenerContainerFactory")
    @Transactional
    public void consume(BillingPrepared billingPrepared) {
        LOGGER.info(String.format("Consumed billingPrepared: %s", billingPrepared));
        try {
            this.billingPrepared = true;
            LOGGER.info("Marked billingPrepared to true");
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce billingPrepared" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce billingPrepared" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("BILLING PREPARED");
        LOGGER.info("****************");
    }


    @KafkaListener(topics= "payment", groupId = "coordinator-paymentCreated", containerFactory="paymentCreatedKafkaListenerContainerFactory")
    @Transactional
    public void consume(PaymentCreated paymentCreated) {
        LOGGER.info(String.format("Consumed paymentCreated: %s", paymentCreated));
        try {
            this.paymentCreated = true;
            LOGGER.info("Marked paymentCreated to true");
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce paymentCreated" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce paymentCreated" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("PAYMENT CREATED");
        LOGGER.info("****************");
    }

    @KafkaListener(topics= "payment", groupId = "coordinator-paymentCancelled", containerFactory="paymentCancelledKafkaListenerContainerFactory")
    @Transactional
    public void consume(PaymentCancelled paymentCancelled) {
        LOGGER.info(String.format("Consumed paymentCancelled: %s", paymentCancelled));
        try {
            this.cancelled = true;
            LOGGER.info("Marked cancelled to true");
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce paymentCancelled" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce paymentCancelled" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("PAYMENT CANCELLED");
        LOGGER.info("****************");
    }


    @KafkaListener(topics= "payment", groupId = "coordinator-paymentAccepted", containerFactory="paymentAcceptedKafkaListenerContainerFactory")
    @Transactional
    public void consume(PaymentAccepted paymentAccepted) {
        LOGGER.info(String.format("Consumed paymentAccepted: %s", paymentAccepted));
        try {
            this.paymentAccepted = true;
            LOGGER.info("Marked paymentAccepted to true");
            CompleteBilling completeBilling = new CompleteBilling(paymentAccepted.getTransactionId());
            kafkaCompleteBillingProducer.send(completeBilling).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce paymentAccepted" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce paymentAccepted" + ex);
        }
        LOGGER.info("****************");
        LOGGER.info("PAYMENT ACCEPTED");
        LOGGER.info("****************");
    }

    //

    @KafkaListener(topics= "stock", groupId = "coordinator-stockBookingFailed", containerFactory="stockBookingFailedKafkaListenerContainerFactory")
    @Transactional
    public void consume(StockBookingFailed stockBookingFailed) {
        LOGGER.info(String.format("Consumed stockBookingFailed: %s", stockBookingFailed));
        try {
            this.cancelled = true;
            LOGGER.info("Marked cancelled to true");

            CancelDelivery cancelDelivery = new CancelDelivery(stockBookingFailed.getTransactionId());
            LOGGER.info(String.format("Sending cancelDelivery %s", cancelDelivery));
            kafkaCancelDeliveryProducer.send(cancelDelivery).get();

            CancelBilling cancelBilling = new CancelBilling(stockBookingFailed.getTransactionId());
            LOGGER.info(String.format("Sending cancelPayment %s", cancelBilling));
            kafkaCancelBillingProducer.send(cancelBilling).get();

            CancelPayment cancelPayment = new CancelPayment(stockBookingFailed.getTransactionId());
            LOGGER.info(String.format("Sending cancelPayment %s", cancelPayment));
            kafkaCancelPaymentProducer.send(cancelPayment).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de traiter ce stockBookingFailed" + ex);
            throw new ImpossibleAddOrderException("Impossible de traiter ce stockBookingFailed" + ex);
        }
    }


    private void tryStartDelivery(UUID transactionId) throws ExecutionException, InterruptedException {
        boolean canBeShipped = this.billingCompleted && this.paymentAccepted && this.stockBooked && !this.cancelled;

        if(canBeShipped) {
            StartDelivery startDelivery = new StartDelivery(transactionId);
            kafkaStartDeliveryProducer.send(startDelivery).get();
            this.billingCompleted = false;
            this.paymentAccepted = false;
            this.stockBooked = false;
        }
    }

}
