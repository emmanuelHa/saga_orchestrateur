package  com.soat.formation.saga.order.domain.model;

import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMMANDE")
public class Order {

    public Order() {
    }

    public Order(String transactionId, Integer quantity, String address) {
        this.transactionId = transactionId;
        this.quantity = quantity;
        this.address = address;
    }

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "transactionId")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    private String transactionId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "address", length = 64)
    private String address;

    public Long getId() {
        return id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getAddress() {
        return address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", quantity=" + quantity +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Order))
            return false;
        Order order = (Order) o;
        return id.equals(order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
