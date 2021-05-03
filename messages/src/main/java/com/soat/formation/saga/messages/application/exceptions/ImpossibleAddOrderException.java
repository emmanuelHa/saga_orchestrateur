package com.soat.formation.saga.messages.application.exceptions;


public class ImpossibleAddOrderException extends RuntimeException {

    public ImpossibleAddOrderException(String message) {
        super(message);
    }


}
