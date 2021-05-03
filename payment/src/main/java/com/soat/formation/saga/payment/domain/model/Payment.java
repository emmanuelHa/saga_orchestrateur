package com.soat.formation.saga.payment.domain.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAYMENT")
public class Payment {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transactionId")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    private String transactionId;

    private float amount;

    private Integer quantity;
    private boolean paymentAccepted;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    public Payment() {}

    public Payment(String transactionId, float amount) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = PaymentStatus.Pending;
    }

    public Payment(String transactionId, Float amount, Integer quantity) {
        this(transactionId, amount);
        this.quantity = quantity;
    }


    public void accept() {
        this.status = PaymentStatus.Accepted;
        this.paymentAccepted = true;
    }

    public void refuse() {
        this.status = PaymentStatus.Refused;
        this.paymentAccepted = false;
    }

    public void toAccept() {
        this.status = PaymentStatus.ToAccept;
    }

    public void toRefuse() {
        this.status = PaymentStatus.Refused;
    }

    public enum PaymentStatus {
        Pending,
        Accepted,
        ToAccept,
        Refused,
        Cancelled
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

    private void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public float getAmount() {
        return amount;
    }

    private void setAmount(float amount) {
        this.amount = amount;
    }

    public boolean isPaymentAccepted() {
        return paymentAccepted;
    }

    public void setPaymentAccepted(boolean paymentAccepted) {
        this.paymentAccepted = paymentAccepted;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", paymentAccepted=" + paymentAccepted +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Payment))
            return false;
        Payment payment = (Payment) o;
        return id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
