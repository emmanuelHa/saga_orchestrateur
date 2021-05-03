package com.soat.formation.saga.stock.infra.dao;

import com.soat.formation.saga.stock.domain.model.Stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDao extends JpaRepository<Stock, Long> {
    Stock findByTransactionId(String transactionId);
}
