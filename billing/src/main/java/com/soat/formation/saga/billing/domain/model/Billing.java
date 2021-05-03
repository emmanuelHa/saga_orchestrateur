package com.soat.formation.saga.billing.domain.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BILLING")
public class Billing {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transactionId")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    private String transactionId;
    @Column
    private String address;
    @Column
    private Integer quantity;
    @Column
    private Float amount;
    @Column
    private boolean stockBooked;
    @Column
    private boolean billingAccepted;
    @Column
    private boolean paid;

    public Billing() {}

    public Billing(String transactionId, String address, Integer quantity, Float amount) {
        this.transactionId = transactionId;
        this.address = address;
        this.quantity = quantity;
        this.amount = amount;
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public boolean isStockBooked() {
        return stockBooked;
    }

    public void setStockBooked(boolean stockBooked) {
        this.stockBooked = stockBooked;
    }

    public boolean isBillingAccepted() {
        return billingAccepted;
    }

    public void setBillingAccepted(boolean BillingAccepted) {
        this.billingAccepted = BillingAccepted;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public void isBeingPaid() {
        this.paid = true;
    }

    @Override
    public String toString() {
        return "Billing{" +
            "id=" + id +
            ", transactionId='" + transactionId + '\'' +
            ", address='" + address + '\'' +
            ", quantity=" + quantity +
            ", amount=" + amount +
            ", stockBooked=" + stockBooked +
            ", billingAccepted=" + billingAccepted +
            ", paid=" + paid +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Billing))
            return false;
        Billing billing = (Billing) o;
        return id.equals(billing.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
