package com.soat.formation.saga.payment.infra.dao;

import com.soat.formation.saga.payment.domain.model.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findOneByTransactionId(String transactionId);

}
