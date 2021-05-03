package com.soat.formation.saga.orchestrateur;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.commands.CancelPayment;
import com.soat.formation.saga.messages.application.events.BookStock;
import com.soat.formation.saga.messages.application.events.CancelBilling;
import com.soat.formation.saga.messages.application.events.CancelDelivery;
import com.soat.formation.saga.messages.application.events.CompleteBilling;
import com.soat.formation.saga.messages.application.events.CreatePayment;
import com.soat.formation.saga.messages.application.events.OrderCreated;
import com.soat.formation.saga.messages.application.events.PrepareBilling;
import com.soat.formation.saga.messages.application.events.PrepareDelivery;
import com.soat.formation.saga.messages.application.events.RegisterOrder;
import com.soat.formation.saga.messages.application.events.StartDelivery;
import com.soat.formation.saga.messages.application.events.UnBookStock;
import com.soat.formation.saga.orchestrateur.service.SagaExecutionCoordinator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class SagaExecutionCoordinatorTest {

    private static SagaExecutionCoordinator saga;

    @Captor
    ArgumentCaptor<RegisterOrder> registerOrderCaptor;

    @Mock
    private KafkaGenericProducer<RegisterOrder> kafkaRegisterOrderProducer;
    @Mock
    private KafkaGenericProducer<PrepareDelivery> kafkaPrepareDeliveryProducer;
    @Mock
    private KafkaGenericProducer<PrepareBilling> kafkaPrepareBillingProducer;
    @Mock
    private KafkaGenericProducer<CompleteBilling> kafkaCompleteBillingProducer;
    @Mock
    private KafkaGenericProducer<BookStock> kafkaBookStockProducer;
    @Mock
    private KafkaGenericProducer<CreatePayment> kafkaCreatePaymentProducer;
    @Mock
    private KafkaGenericProducer<StartDelivery> kafkaStartDeliveryProducer;
    @Mock
    private KafkaGenericProducer<CancelDelivery> kafkaCancelDeliveryProducer;
    @Mock
    private KafkaGenericProducer<CancelBilling> kafkaCancelBillingProducer;
    @Mock
    private KafkaGenericProducer<UnBookStock> kafkaUnbookStockProducer;
    @Mock
    private KafkaGenericProducer<CancelPayment> kafkaCancelPaymentProducer;


    @BeforeEach
    private void init() {
        ListenableFuture<SendResult<String, RegisterOrder>> listenableFuture = mock(ListenableFuture.class);
        when(kafkaRegisterOrderProducer.send(any(RegisterOrder.class))).thenReturn(listenableFuture);
    saga = new SagaExecutionCoordinator( kafkaRegisterOrderProducer,
                                         kafkaPrepareBillingProducer,
                                         kafkaCompleteBillingProducer,
                                         kafkaCancelBillingProducer,
                                         kafkaBookStockProducer,
                                         kafkaUnbookStockProducer,
                                         kafkaCreatePaymentProducer,
                                         kafkaCancelPaymentProducer,
                                         kafkaStartDeliveryProducer,
                                        kafkaCancelDeliveryProducer,
                                        kafkaPrepareDeliveryProducer
        );
    }

    @Test
    public void whenOrderCreatedConsumedThenRegisterOrderSent() {
        OrderCreated orderCreated = createOrderCreated("20 rue des champs", 10);
        saga.consume(orderCreated);
        verify(kafkaRegisterOrderProducer).send(registerOrderCaptor.capture());
        assertThat(registerOrderCaptor.getValue())
                .isEqualToComparingOnlyGivenFields(new RegisterOrder(UUID.randomUUID(),
                                                                     "20 rue des champs",
                                                                     10),
                                                   "address", "quantity");
    }

    private OrderCreated createOrderCreated(String address, int quantity) {
        OrderCreated orderCreated = new OrderCreated(quantity, address);
        return orderCreated;
    }


}
