package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class CancelBilling implements Event {

    private UUID transactionId;

    // DO NOT REMOVE IT
    public CancelBilling() {}

    public CancelBilling(UUID transactionId) {
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
        return "CancelBilling{" +
                "transactionId=" + transactionId +
                '}';
    }
}
