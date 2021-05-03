package com.soat.formation.saga.messages.application.events;

import java.util.UUID;

public class PrepareBilling implements Event {

    private UUID transactionId;
    private String address;
    private Integer quantity;
    private Float amount;

    // DO NOT REMOVE IT
    public PrepareBilling() {}

    public PrepareBilling(UUID transactionId, String address, Integer quantity, Float amount) {

        this.transactionId = transactionId;
        this.address = address;
        this.quantity = quantity;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public String getAddress() {
        return address;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getAmount() {
        return amount;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PrepareBilling{" +
                "transactionId=" + transactionId +
                ", address='" + address + '\'' +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}
