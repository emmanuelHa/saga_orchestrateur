package com.soat.formation.saga.stock.application.controller;

import com.soat.formation.saga.stock.application.exception.ImpossibleAddStockException;
import com.soat.formation.saga.stock.application.exception.StockNotFoundException;
import com.soat.formation.saga.stock.domain.model.Stock;
import com.soat.formation.saga.stock.infra.dao.StockDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StockController {

    private final Logger LOGGER = LoggerFactory.getLogger(com.soat.formation.saga.stock.application.controller.StockController.class);

    @Autowired
    StockDao stockDao;

    @GetMapping(value = "/stocks")
    public List<Stock> getStocks() {
        List<Stock> stocks = stockDao.findAll();
        return stocks;
    }

    @GetMapping(value = "/stock/{uuid}")
    public Stock getStock(@PathVariable String uuid) {
        LOGGER.info(String.format("getStock for transactionId %s", uuid));
        Stock stock = stockDao.findByTransactionId(uuid);

        if(stock == null) {
            throw new StockNotFoundException("Ce stock n'existe pas");
        }

        return stock;
    }


    @PostMapping(value = "/stock/add")
    public ResponseEntity<Stock> ajouterStock(@RequestBody Stock stock) {

        Stock nouveauStock = stockDao.save(stock);

        LOGGER.info("NEW STOCK ADDED : " + nouveauStock.getQuantity());
        if(nouveauStock == null) {
            throw new ImpossibleAddStockException("Impossible d'ajouter cette stock");
        }
        return new ResponseEntity<>(stock, HttpStatus.CREATED);
    }

}
