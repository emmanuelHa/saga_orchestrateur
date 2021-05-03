package com.soat.formation.saga.billing.infra.dao;

import com.soat.formation.saga.billing.domain.model.Billing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {
    Billing findByTransactionId(String transactionId);
}
