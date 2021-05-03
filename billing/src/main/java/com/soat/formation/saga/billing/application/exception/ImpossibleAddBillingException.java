package com.soat.formation.saga.billing.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAddBillingException extends RuntimeException {

    public ImpossibleAddBillingException(String message) {
        super(message);
    }


}
