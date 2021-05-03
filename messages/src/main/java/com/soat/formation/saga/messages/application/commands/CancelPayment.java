package com.soat.formation.saga.messages.application.commands;

import com.soat.formation.saga.messages.application.events.Event;

import java.util.UUID;

public class CancelPayment implements Event {

    private UUID transactionId;

    public CancelPayment() {}

    public CancelPayment(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "CancelPayment{" +
                "transactionId=" + transactionId +
                '}';
    }
}
