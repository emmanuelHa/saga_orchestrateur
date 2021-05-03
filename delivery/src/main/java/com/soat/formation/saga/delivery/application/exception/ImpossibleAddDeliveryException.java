package com.soat.formation.saga.delivery.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAddDeliveryException extends RuntimeException {

    public ImpossibleAddDeliveryException(String message) {
        super(message);
    }


}
