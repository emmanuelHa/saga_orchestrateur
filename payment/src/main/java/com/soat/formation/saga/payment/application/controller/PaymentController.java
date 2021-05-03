package com.soat.formation.saga.payment.application.controller;

import com.soat.formation.saga.payment.application.exception.ImpossibleAddPaymentException;
import com.soat.formation.saga.payment.domain.model.Payment;
import com.soat.formation.saga.payment.domain.service.PaymentService;
import com.soat.formation.saga.payment.infra.dao.PaymentRepository;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentController {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    PaymentService paymentService;

    @GetMapping(value = "/payments")
    public List<Payment> recupererListPaiements() {
        return paymentRepository.findAll();
    }

    @GetMapping(value = "/payments/{uuid}")
    public String recupererPaiement(@PathVariable String uuid) {
        LOGGER.info(String.format("/payments/%s", uuid));
        Payment payment = paymentRepository.findOneByTransactionId(uuid);
        if(payment == null) {
            return null;
        }
        return payment.getStatus().name();
    }

    @PostMapping(value = "/payment/accept")
    public ResponseEntity<HttpStatus> acceptPayment(@RequestBody String uuidJson) {
        try {
            // TODO use service
            JSONObject jsonObject = new JSONObject(uuidJson);
            String uuid = (String) jsonObject.get("uuid");
            Payment payment = paymentRepository.findOneByTransactionId(uuid);
            payment.toAccept();
            String newPaymentTransactionId = paymentService.save(payment);
            if (newPaymentTransactionId == null) {
                throw new ImpossibleAddPaymentException("Impossible d'ajouter ce payment");
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception ex) {
            LOGGER.error("Impossible d'ajouter ce paiement", ex);
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce paiement");
        }
    }

    @PostMapping(value = "/payment/refuse")
    public ResponseEntity<HttpStatus> refusePayment(@RequestBody String uuidJson) {
        try {
            // TODO use service
            JSONObject jsonObject = new JSONObject(uuidJson);
            String uuid = (String) jsonObject.get("uuid");
            Payment payment = paymentRepository.findOneByTransactionId(uuid);
            payment.toRefuse();
            String newPaymentTransactionId = paymentService.save(payment);
            if (newPaymentTransactionId == null) {
                throw new ImpossibleAddPaymentException(String.format("Impossible d'ajouter ce payment %s", payment));
            }
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception ex) {
            LOGGER.error("Impossible d'ajouter ce paiement", ex);
            throw new ImpossibleAddPaymentException("Impossible d'ajouter ce paiement");
        }
    }


}
