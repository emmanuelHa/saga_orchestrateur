package com.soat.formation.saga.stock.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ImpossibleAddStockException extends RuntimeException {

    public ImpossibleAddStockException(String message) {
        super(message);
    }


}
