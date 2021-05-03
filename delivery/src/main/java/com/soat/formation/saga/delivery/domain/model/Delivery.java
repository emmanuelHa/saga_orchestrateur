package com.soat.formation.saga.delivery.domain.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DELIVERY")
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transactionId")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    private String transactionId;
    private String address;
    private boolean stockBooked;
    private boolean paymentAccepted;
    private boolean shipped;
    private boolean billingCompleted;


    public Delivery() {
    }

    public Delivery(UUID uuid, String address) {
        this.transactionId = uuid.toString();
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isStockBooked() {
        return stockBooked;
    }

    public void setStockBooked(boolean stockBooked) {
        this.stockBooked = stockBooked;
    }

    public void paymentAccepted() {
        this.paymentAccepted = true;
    }

    public boolean isPaymentAccepted() {
        return paymentAccepted;
    }

    public boolean isShipped() {
        return shipped;
    }

    public void setShipped(boolean shipped) {
        this.shipped = shipped;
    }

    public void ship() {
        this.shipped = true;
        this.billingCompleted = true;
        this.paymentAccepted = true;
        this.stockBooked = true;
    }

    public boolean isBillingCompleted() {
        return billingCompleted;
    }

    public void billingCompleted() {
        this.billingCompleted = true;
    }

    public void stockBooked() {
        this.stockBooked = true;
    }

    @Override
    public String toString() {
        return "Delivery{" +
            "id=" + id +
            ", transactionId='" + transactionId + '\'' +
            ", address='" + address + '\'' +
            ", stockBooked=" + stockBooked +
            ", paymentAccepted=" + paymentAccepted +
            ", shipped=" + shipped +
            ", billingCompleted=" + billingCompleted +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Delivery))
            return false;
        Delivery delivery = (Delivery) o;
        return id.equals(delivery.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
