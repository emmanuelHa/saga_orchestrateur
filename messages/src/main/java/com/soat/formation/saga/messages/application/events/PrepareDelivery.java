package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class PrepareDelivery implements Event {

    private UUID transactionId;
    private String address;

    // DO NOT REMOVE IT
    public PrepareDelivery() {}

    public PrepareDelivery(UUID transactionId, String address) {
        this.transactionId = transactionId;
        this.address = address;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getAddress() {
        return address;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "PrepareDelivery{" +
                "transactionId=" + transactionId +
                ", address='" + address + '\'' +
                '}';
    }
}
