package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class StockBookingFailed implements Event {

    private UUID transactionId;
    private final Date date = new Date();
    private Integer quantity;
    private String address;

    public StockBookingFailed() {}
    public StockBookingFailed(UUID id) {
        this.transactionId = id;
    }

    public enum status {
        StockBookingFailed;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
        return "StockBookingFailed{" +
            "transactionId=" + transactionId +
            ", date=" + date +
            ", quantity=" + quantity +
            ", address='" + address + '\'' +
            '}';
    }
}
