package  com.soat.formation.saga.order.infra.dao;

import com.soat.formation.saga.order.domain.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
