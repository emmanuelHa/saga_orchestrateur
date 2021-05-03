package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class StartDelivery implements Event {

    private UUID transactionId;

    // DO NOT REMOVE IT
    public StartDelivery() {}

    public StartDelivery(UUID transactionId) {
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
        return "StartDelivery{" +
                "transactionId=" + transactionId +
                '}';
    }
}
