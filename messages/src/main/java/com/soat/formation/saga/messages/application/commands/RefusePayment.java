package com.soat.formation.saga.messages.application.commands;

import com.soat.formation.saga.messages.application.events.Event;

import java.util.UUID;

public class RefusePayment implements Event {

    private  UUID transactionId;

    public RefusePayment() {}

    public RefusePayment(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "RefusePayment{" +
            "transactionId=" + transactionId +
            '}';
    }


}
