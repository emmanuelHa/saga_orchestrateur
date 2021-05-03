package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class UnBookStock implements Event {

    private UUID transactionId;
    private Integer quantity;

    // DO NOT REMOVE IT
    public UnBookStock() {}

    public UnBookStock(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UnBookStock(UUID transactionId, Integer quantity) {
        this.transactionId = transactionId;
        this.quantity = quantity;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "UnBookStock{" +
                "transactionId=" + transactionId +
                ", quantity=" + quantity +
                '}';
    }


}
