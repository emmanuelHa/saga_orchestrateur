package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class StockBooked implements Event {

    private UUID transactionId;
    private float amount;
    private final Date date = new Date();

    public StockBooked() {}

    public StockBooked(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public StockBooked(UUID transactionId, float amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Date getDate() {
        return date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "StockBooked{" +
            "transactionId=" + transactionId +
            ", amount=" + amount +
            ", date=" + date +
            '}';
    }
}
