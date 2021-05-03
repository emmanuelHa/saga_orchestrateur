package com.soat.formation.saga.delivery.application.controller;

import com.soat.formation.saga.delivery.domain.model.Delivery;
import com.soat.formation.saga.delivery.infra.dao.DeliveryRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeliveryController {

    private final Logger LOGGER = LoggerFactory.getLogger(DeliveryController.class);

    @Autowired
    DeliveryRepository deliveryRepository;

    @GetMapping(value = "/deliveries")
    public List<Delivery> recupererListPaiements() {
        LOGGER.debug("/deliveries");
        return deliveryRepository.findAll();
    }


}
