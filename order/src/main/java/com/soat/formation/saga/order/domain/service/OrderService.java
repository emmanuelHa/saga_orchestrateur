package  com.soat.formation.saga.order.domain.service;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.OrderRegistered;
import com.soat.formation.saga.messages.application.events.RegisterOrder;
import com.soat.formation.saga.order.application.web.exception.ImpossibleAddOrderException;
import com.soat.formation.saga.order.domain.model.Order;
import com.soat.formation.saga.order.infra.dao.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private OrderRepository orderRepository;
    private KafkaGenericProducer<RegisterOrder> kafkaRegisterOrderProducer;
    private KafkaGenericProducer<OrderRegistered> kafkaOrderRegisteredProducer;

    @Autowired
    public OrderService(OrderRepository orderRepository,
                        AbstractKafkaGenericProducer<RegisterOrder> kafkaRegisterOrderProducer,
                        AbstractKafkaGenericProducer<OrderRegistered> kafkaOrderRegisteredProducer
                        ) {
        this.orderRepository = orderRepository;
        this.kafkaRegisterOrderProducer = kafkaRegisterOrderProducer.mapEventTypeToTopic(RegisterOrder.class, "order");
        this.kafkaOrderRegisteredProducer = kafkaOrderRegisteredProducer.mapEventTypeToTopic(OrderRegistered.class, "order");
    }


    @KafkaListener(topics = "order", groupId = "coordinator-registerOrder", containerFactory="registerOrderKafkaListenerContainerFactory")
    @Transactional
    public void consume(RegisterOrder registerOrder) {
        LOGGER.info(String.format("Consumed registerOrder: %s", registerOrder));
        try {
            Order order = new Order(registerOrder.getTransactionId().toString(),
                                    registerOrder.getQuantity(),
                                    registerOrder.getAddress());

            orderRepository.save(order);
            OrderRegistered orderRegistered = new OrderRegistered(registerOrder.getQuantity(), registerOrder.getAddress(),
                                                                  registerOrder.getTransactionId());
            int priceUnit = 5;
            orderRegistered.setAmount((float) (orderRegistered.getQuantity()*priceUnit));
            LOGGER.info(String.format("Sending orderRegistered with event id %s", orderRegistered.getTransactionId()));
            /*if(Math.random() > 0.5) {
                throw new IllegalStateException("Simulate orderRegistered Error");
            }*/
            kafkaOrderRegisteredProducer.send(orderRegistered).get();
        }
        catch(Exception ex) {
            LOGGER.error("Impossible de faire ce registerOrder");
            throw new ImpossibleAddOrderException("Impossible de faire ce registerOrder");
        }
    }

}
