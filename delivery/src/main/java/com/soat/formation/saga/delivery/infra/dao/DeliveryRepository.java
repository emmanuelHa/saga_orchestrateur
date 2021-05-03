package com.soat.formation.saga.delivery.infra.dao;

import com.soat.formation.saga.delivery.domain.model.Delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    // TODO findByTransactionId
    Delivery findByTransactionId(String transactionId);

}
