package com.soat.formation.saga.messages.application.commands;

import com.soat.formation.saga.messages.application.events.Event;

import java.util.UUID;

public class AcceptPayment implements Event {

    private  UUID transactionId;

    public AcceptPayment() {}

    public AcceptPayment(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "AcceptPayment{" +
                "transactionId=" + transactionId +
                '}';
    }
}
