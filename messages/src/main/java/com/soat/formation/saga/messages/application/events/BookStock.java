package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class BookStock implements Event {

    private UUID transactionId;
    private Integer quantity;

    // DO NOT REMOVE IT
    public BookStock() {}

    public BookStock(UUID transactionId, Integer quantity) {this.transactionId = transactionId;
        this.quantity = quantity;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BookStock{" +
                "transactionId=" + transactionId +
                ", quantity=" + quantity +
                '}';
    }
}
