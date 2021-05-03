package com.soat.formation.saga.stock.infra.dao;

import com.soat.formation.saga.stock.domain.model.Stock;
import com.soat.formation.saga.stock.domain.model.StockBooking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockBookingDao extends JpaRepository<StockBooking, Long> {

    StockBooking findOneByTransactionId(String transactionId);
}
