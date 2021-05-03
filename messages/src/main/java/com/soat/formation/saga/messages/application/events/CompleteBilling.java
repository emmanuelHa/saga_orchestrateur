package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class CompleteBilling implements Event {

    private UUID transactionId;

    public CompleteBilling() {}

    public CompleteBilling(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "CompleteBilling{" +
                "transactionId=" + transactionId +
                '}';
    }
}
