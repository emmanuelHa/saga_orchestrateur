package  com.soat.formation.saga.order.application.web.controller;

import com.soat.formation.saga.order.domain.model.Order;
import com.soat.formation.saga.order.infra.dao.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    OrderRepository orderRepository;

    @GetMapping(value = "/orders")
    public List<Order> getOrders() {
        LOGGER.debug("/orders");
        return orderRepository.findAll();
    }

}
