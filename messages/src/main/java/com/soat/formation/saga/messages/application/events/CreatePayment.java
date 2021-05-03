package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class CreatePayment implements Event {

    private UUID transactionId;
    private Float amount;
    private Integer quantity;

    // DO NOT REMOVE IT
    public CreatePayment() {}

    public CreatePayment(UUID transactionId, Float amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public CreatePayment(UUID transactionId, Float amount, Integer quantity) {
        this(transactionId, amount);
        this.quantity = quantity;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Float getAmount() {
        return amount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CreatePayment{" +
                "transactionId=" + transactionId +
                ", amount=" + amount +
                ", quantity=" + quantity +
                '}';
    }
}
