package com.soat.formation.saga.messages.application.events;

import java.util.Date;
import java.util.UUID;

public class OrderCreated implements Event {

    private final UUID transactionId = UUID.randomUUID();
    private final Date date = new Date();
    private Integer quantity;
    private String address;

    public enum status {
        OrderRegistered, OrderCreated;

    }

    public OrderCreated() {}

    public OrderCreated(Integer quantity, String address) {
        this.quantity = quantity;
        this.address = address;
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



    @Override
    public String toString() {
        return "OrderCreated{" +
            "transactionId=" + transactionId +
            ", date=" + date +
            ", quantity=" + quantity +
            ", address='" + address + '\'' +
            '}';
    }
}
