package com.soat.formation.saga.stock.domain.service;

import com.soat.formation.saga.infra.config.AbstractKafkaGenericProducer;
import com.soat.formation.saga.infra.config.KafkaGenericProducer;
import com.soat.formation.saga.messages.application.events.BookStock;
import com.soat.formation.saga.messages.application.events.StockBooked;
import com.soat.formation.saga.messages.application.events.StockBookingFailed;
import com.soat.formation.saga.messages.application.events.UnBookStock;
import com.soat.formation.saga.stock.application.exception.ImpossibleAddStockException;
import com.soat.formation.saga.stock.domain.model.Stock;
import com.soat.formation.saga.stock.domain.model.StockBooking;
import com.soat.formation.saga.stock.infra.dao.StockBookingDao;
import com.soat.formation.saga.stock.infra.dao.StockDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import javax.transaction.Transactional;

@Service
public class StockService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockService.class);

    private StockBookingDao stockBookingDao;
    private StockDao stockDao;
    private KafkaGenericProducer<StockBooked> kafkaStockProducerConfig;
    private KafkaGenericProducer<StockBookingFailed> kafkaStockBookingFailedProducer;

    @Autowired
    public StockService(StockDao stockDao, StockBookingDao stockBookingDao,
                        AbstractKafkaGenericProducer<StockBooked> kafkaStockProducerConfig,
                        AbstractKafkaGenericProducer<StockBookingFailed> kafkaStockBookingFailedProducer) {
        this.stockDao = stockDao;
        this.stockBookingDao = stockBookingDao;
        this.kafkaStockProducerConfig = kafkaStockProducerConfig.mapEventTypeToTopic(StockBooked.class, "stock");
        this.kafkaStockBookingFailedProducer = kafkaStockBookingFailedProducer.mapEventTypeToTopic(StockBookingFailed.class, "stock");
    }

    @KafkaListener(topics = "stock", groupId = "stock-bookStock", containerFactory = "bookStockKafkaListenerContainerFactory")
    @Transactional
    public void consume(BookStock bookStock) {
        LOGGER.info(String.format("Consumed message bookStock : %s", bookStock));
        try {

            List<Stock> stocks = stockDao.findAll();
            if(stocks.size() == 0) {
                throw new IllegalStateException("No Stock available");
            }
            var existingStock = stocks.stream().skip(stocks.size() - 1).findFirst().get();
            existingStock.setTransactionId(bookStock.getTransactionId().toString());
            Integer existingStockQuantity = existingStock.getQuantity();
            UUID transactionId = bookStock.getTransactionId();
            Integer wantedQuantity = bookStock.getQuantity();
            if (existingStockQuantity < wantedQuantity) {
                sendStockBookingFailed(transactionId);
            }
            else {
                existingStock.setQuantity(existingStockQuantity - wantedQuantity);
                stockDao.save(existingStock);
                stockBookingDao.save(new StockBooking(transactionId, wantedQuantity));
                StockBooked stockBooked = new StockBooked(transactionId, wantedQuantity);
                sendStockBooked(stockBooked);
            }

        }  catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            throw new ImpossibleAddStockException("Impossible d'ajouter ce stock");
        }
    }

    @KafkaListener(topics = "stock", groupId = "stock-unBookStock", containerFactory = "unBookStockKafkaListenerContainerFactory")
    @Transactional
    public void consume(UnBookStock unBookStock) {
        LOGGER.info(String.format("Consumed message unBookStock : %s", unBookStock));
        try {
            List<Stock> stocks = stockDao.findAll();
            var existingStock = stocks.stream().skip(stocks.size() - 1).findFirst().orElseThrow(() -> new IllegalStateException("No Stock available"));
            Integer existingStockQuantity = existingStock.getQuantity();
            UUID transactionId = unBookStock.getTransactionId();
            StockBooking stockBooking = stockBookingDao.findOneByTransactionId(transactionId.toString());
            if(stockBooking == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun booking avec transaction id " + transactionId.toString());
            }
            List<StockBooking> stockBookings = stockBookingDao.findAll();
            stockBookings.remove(stockBooking);
            int newQuantity = existingStockQuantity + unBookStock.getQuantity();
            existingStock.setQuantity(newQuantity);
            LOGGER.info(String.format("STOCK transaction id %s quantité restante : %s", existingStock, newQuantity));
        }  catch (Exception ex) {
            ex.printStackTrace();
            throw new ImpossibleAddStockException("UnBookStock Impossible " + ex.getMessage());
        }
    }


    private void sendStockBooked(StockBooked stockBooked) throws InterruptedException, ExecutionException {
        LOGGER.info("SENDING stockBooked " + stockBooked);
        kafkaStockProducerConfig.send(stockBooked).get();
    }

    private void sendStockBookingFailed(UUID transactionId) throws InterruptedException, ExecutionException {
        LOGGER.info("Not enough Stock left");
        StockBookingFailed stockBookingFailed = new StockBookingFailed(transactionId);
        kafkaStockBookingFailedProducer.send(stockBookingFailed).get();
    }
}
