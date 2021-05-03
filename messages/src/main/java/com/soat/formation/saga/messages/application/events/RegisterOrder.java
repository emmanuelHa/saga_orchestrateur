package com.soat.formation.saga.messages.application.events;

import java.util.Objects;
import java.util.UUID;

public class RegisterOrder implements Event {

    private UUID transactionId;
    private String address;
    private Integer quantity;

    // DO NOT REMOVE IT
    public RegisterOrder() {}

    public RegisterOrder(UUID transactionId, String address, Integer quantity) {

        this.transactionId = transactionId;
        this.address = address;
        this.quantity = quantity;
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

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RegisterOrder{" +
                "transactionId=" + transactionId +
                ", address='" + address + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RegisterOrder))
            return false;
        RegisterOrder that = (RegisterOrder) o;
        return transactionId.equals(that.transactionId) &&
                address.equals(that.address) &&
                quantity.equals(that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, address, quantity);
    }
}
