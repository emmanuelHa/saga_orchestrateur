package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class DeliveryPrepared implements Event {

    private UUID transactionId;
    private final Date date = new Date();

    public enum status {
        DeliveryPrepared;
    }

    public DeliveryPrepared() {}

    public DeliveryPrepared(UUID transactionId) {
        this.transactionId = transactionId;
    }

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
        return "DeliveryPrepared{" +
            "transactionId=" + transactionId +
            ", date=" + date +
            '}';
    }
}
