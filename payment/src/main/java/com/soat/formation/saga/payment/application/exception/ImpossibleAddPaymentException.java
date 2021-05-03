package com.soat.formation.saga.payment.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAddPaymentException extends RuntimeException {

    public ImpossibleAddPaymentException(String message) {
        super(message);
    }


}
