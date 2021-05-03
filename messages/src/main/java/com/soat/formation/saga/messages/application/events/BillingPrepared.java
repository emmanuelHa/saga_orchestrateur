package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class BillingPrepared implements Event {

    private UUID transactionId;

    // DO NOT REMOVE IT
    public BillingPrepared() {}

    public BillingPrepared(UUID transactionId) {
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
        return "BillingPrepared{" +
                "transactionId=" + transactionId +
                '}';
    }
}
