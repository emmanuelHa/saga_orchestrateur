package com.soat.formation.saga.clientui.application.controller;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.commands.AcceptPayment;
import com.soat.formation.saga.messages.application.commands.RefusePayment;
import com.soat.formation.saga.messages.application.events.BillingCompleted;
import com.soat.formation.saga.messages.application.events.OrderCreated;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@RestController
public class HomeController {

    private static final String PENDING = "Pending";
    private final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

    // TODO use DTO & HARD CODED port
    private static final String ENDPOINT_PAYMENTS = "http://localhost:9004/payments/";
    private final RestTemplate paymentTemplate;
    private KafkaGenericProducer<OrderCreated> kafkaOrderCreatedProducer;
    private KafkaGenericProducer<AcceptPayment> kafkaAcceptPaymentProducer;
    private KafkaGenericProducer<RefusePayment> kafkaRefusePaymentProducer;

    @Autowired
    public HomeController(RestTemplate template,
                          @NotNull AbstractKafkaGenericProducer<OrderCreated> kafkaOrderCreatedProducer,
                          @NotNull AbstractKafkaGenericProducer<AcceptPayment> kafkaAcceptPaymentProducer,
                          @NotNull AbstractKafkaGenericProducer<RefusePayment> kafkaRefusePaymentProducer
    ) {
        paymentTemplate = template;
        this.kafkaOrderCreatedProducer = kafkaOrderCreatedProducer.mapEventTypeToTopic(OrderCreated.class, "order");
        this.kafkaAcceptPaymentProducer = kafkaAcceptPaymentProducer.mapEventTypeToTopic(AcceptPayment.class, "payment");
        this.kafkaRefusePaymentProducer = kafkaRefusePaymentProducer.mapEventTypeToTopic(RefusePayment.class, "payment");
    }


    @GetMapping(value = "/hello")
    public String sayHello() {
        return "Hello !";
    }


    @PostMapping(value = "/publish/order")
    @ResponseBody
    public ResponseEntity<HttpStatus> orderNew(@RequestParam Integer quantity, @RequestParam String address) {
        try {
            LOGGER.info("/publish/order");
            OrderCreated orderCreated = new OrderCreated(quantity, address);
            LOGGER.info(String.format("Sending orderCreated with event id %s", orderCreated.getTransactionId()));
            kafkaOrderCreatedProducer.send(orderCreated).get();
        } catch (Exception ex) {
            LOGGER.error("Impossible d'ajouter ce paiement", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping(value = "/publish/payment/accept/{uuid}}")
    @ResponseBody
    @Transactional
    public ResponseEntity<HttpStatus> acceptPayment(@PathVariable String uuid) {
        try {
            LOGGER.info(String.format("/publish/payment/accept/%s", uuid));

            // TODO use DTO & not HARD CODED port
            String paymentStatus = paymentTemplate.getForObject("http://localhost:9004/payments/" + uuid, String.class);

            if(paymentStatus == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(!PENDING.equals(paymentStatus)) {
                LOGGER.error(String.format("Le paiement %s n'est pas au status PENDING", uuid));
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            askPaymentModuleToAcceptPaymentStatus(uuid);

            AcceptPayment acceptPaymentCommand = new AcceptPayment(UUID.fromString(uuid));
            LOGGER.info(String.format("Sending acceptPayment with transaction id %s", acceptPaymentCommand.getTransactionId()));
            kafkaAcceptPaymentProducer.send(acceptPaymentCommand).get();
        } catch (Exception ex) {
            LOGGER.error("Impossible d'ajouter ce paiement");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PostMapping(value = "/publish/payment/refuse/{uuid}}")
    @ResponseBody
    @Transactional
    public ResponseEntity<HttpStatus> refusePayment(@PathVariable String uuid) {
        try {
            LOGGER.info(String.format("/publish/payment/refuse/%s", uuid));

            String paymentStatus = paymentTemplate.getForObject(ENDPOINT_PAYMENTS + uuid, String.class);

            if(paymentStatus == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            if(!PENDING.equals(paymentStatus)) {
                LOGGER.error(String.format("Le paiement %s n'est pas au status PENDING", uuid));
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            askPaymentModuleToRefusePaymentStatus(uuid);

            RefusePayment refusePayment = new RefusePayment(UUID.fromString(uuid));
            LOGGER.info(String.format("Sending RefusePayment with transaction id %s", refusePayment.getTransactionId()));
            kafkaRefusePaymentProducer.send(refusePayment).get();
        } catch (Exception ex) {
            LOGGER.error("Impossible d'ajouter ce paiement" + ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void askPaymentModuleToAcceptPaymentStatus(String uuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject paymentToUpdateStatusJson = new JSONObject();
        paymentToUpdateStatusJson.put("uuid", uuid);
        HttpEntity<String> request = new HttpEntity<>(paymentToUpdateStatusJson.toString(), headers);
        paymentTemplate.postForEntity("http://localhost:9004/payment/accept", request, HttpStatus.class);
    }

    private void askPaymentModuleToRefusePaymentStatus(String uuid) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject paymentToUpdateStatusJson = new JSONObject();
        paymentToUpdateStatusJson.put("uuid", uuid);
        HttpEntity<String> request = new HttpEntity<>(paymentToUpdateStatusJson.toString(), headers);
        paymentTemplate.postForEntity("http://localhost:9004/payment/refuse", request, HttpStatus.class);
    }

}
