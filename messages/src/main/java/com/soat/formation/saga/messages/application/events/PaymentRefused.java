package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class PaymentRefused implements Event {

    private final Date date = new Date();
    private UUID transactionId;
    private Float amount;
    private String address;
    private Integer quantity;

    public enum status {
        PaymentRefused;

    }
    public PaymentRefused() {}

    public PaymentRefused(UUID transactionId, Float amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public PaymentRefused(UUID transactionId, Float amount, Integer quantity) {
        this(transactionId, amount);
        this.quantity = quantity;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDate() {
        return date;
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
        return "PaymentRefused{" +
                "date=" + date +
                ", transactionId=" + transactionId +
                ", amount=" + amount +
                ", address='" + address + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
