package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class BillingCancelled implements Event {

    private final Date date = new Date();

    public enum status {
        BillingCancelled;
    }
    public BillingCancelled() {}

    public BillingCancelled(UUID transactionId) {
        this.transactionId = transactionId;
    }

    private UUID transactionId;

    public UUID getTransactionId() {
        return transactionId;
    }

    public Date getDate() {
        return date;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "BillingCancelled{" +
            "date=" + date +
            ", transactionId=" + transactionId +
            '}';
    }
}
