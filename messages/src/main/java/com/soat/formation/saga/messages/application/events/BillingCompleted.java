package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class BillingCompleted implements Event {

    private UUID transactionId;
    private Float amount;
    private final Date date = new Date();

    public enum status {
        BillingCompleted;
    }

    public BillingCompleted() {}

    public BillingCompleted(UUID transactionId, Float amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public BillingCompleted(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Date getDate() {
        return date;
    }

    public Float getAmount() {
        return amount;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BillingCompleted{" +
            "transactionId=" + transactionId +
            ", amount=" + amount +
            ", date=" + date +
            '}';
    }
}
