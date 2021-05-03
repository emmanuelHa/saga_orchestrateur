package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class CancelDelivery implements Event {

    private UUID transactionId;

    // DO NOT REMOVE IT
    public CancelDelivery() {}

    public CancelDelivery(UUID transactionId) {
        this.transactionId = transactionId;}

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "CancelDelivery{" +
                "transactionId=" + transactionId +
                '}';
    }
}
